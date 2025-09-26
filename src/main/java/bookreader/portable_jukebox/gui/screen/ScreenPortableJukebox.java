package bookreader.portable_jukebox.gui.screen;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenPortableJukebox extends ScreenContainerAbstract {
    private int top;
    private int left;
    private final Player player;
    private final ItemStack portable_jukebox_item;
    public ScreenPortableJukebox(ContainerInventory inventory, ItemStack portable_jukebox_item) {
        super(new MenuPortableJukebox(inventory, portable_jukebox_item));
        this.player = inventory.player;
        this.portable_jukebox_item = portable_jukebox_item;
    }
    
    public void init()
    {
        super.init();
        this.left = (this.width - this.xSize) / 2;
        this.top = (this.height - this.ySize) / 2;
        this.buttons.clear();
        this.buttons.add(new ButtonElement(0, left + 7 + 18 + 1, top + 7, 18, 18, "||")); // I18n.getInstance().translateKey("gui.achievements.button.done")
        this.buttons.add(new ButtonElement(1, left + 7 + (18 + 1) * 2, top + 7, 18, 18, "<"));
    }

    @Override
    protected void buttonClicked(ButtonElement button)
    {
        PortableJukebox.LOGGER.info("Button Pressed!");
        if (button.id == 0)
        {
            if (!SoundUtils.started())
            {
                SoundUtils.playRecordAt(((PortableJukeboxItem)portable_jukebox_item.getItem()).getPlayingDisk(portable_jukebox_item), player);
                PortableJukebox.LOGGER.info("Started song");
            }
            else if (SoundUtils.playing())
            {
                SoundUtils.pause();
                PortableJukebox.LOGGER.info("Paused song");
            }
            else
            {
                SoundUtils.unpause();
                PortableJukebox.LOGGER.info("Unpaused song");
            }
        }
        else if (button.id == 1 && button.enabled)
        {
            PortableJukebox.LOGGER.info("Stopping track");
            SoundUtils.stop();
        }
        super.buttonClicked(button);
    }

    public void render(int mx, int my, float partialTick)
    {
        if (!SoundUtils.playing())
        {
            buttons.get(0).displayString = "|>";
        } else
        {
            buttons.get(0).displayString = "||";
        }
        // TODO: there has to be a better way
        buttons.get(1).enabled = SoundUtils.started_noupdate();
        super.render(mx, my, partialTick);
    }

    // private ItemDiscMusic disc;
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        // this.mc.textureManager.loadTexture("/gui/container.png").bind();
        this.mc.textureManager.loadTexture("/assets/portable_jukebox/textures/gui/container/portable_jukebox_screen.png").bind();
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize/*, 0.006, 0.006*/);
    }
}