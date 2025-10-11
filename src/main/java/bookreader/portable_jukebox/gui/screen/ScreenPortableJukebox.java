package bookreader.portable_jukebox.gui.screen;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.SongControlPacketS2C;
import bookreader.portable_jukebox.packet.SongControlPacketC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import turniplabs.halplibe.helper.network.NetworkHandler;

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
//		int i = 113;
//		this.ySize = i + PortableJukeboxItem.STORAGE_SIZE * 2;
		this.left = (this.width - this.xSize) / 2;
		this.top = (this.height - this.ySize) / 2;
		this.buttons.clear();
		this.buttons.add(new ButtonElement(0, left + 7 + 18 + 1, top + 7, 18, 18, "||")); // I18n.getInstance().translateKey("gui.achievements.button.done")
		this.buttons.add(new ButtonElement(1, left + 7 + (18 + 1) * 2, top + 7, 18, 18, "<"));
//		this.passEvents = false;
    }

//	public void removed()
//	{
//		super.removed();
//		this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
//	}

    @Override
    protected void buttonClicked(ButtonElement button)
    {
        PortableJukebox.LOGGER.debug("Button Pressed!");
        if (button.id == 0)
        {
            if (!SoundUtils.started())
            {
                ItemDiscMusic disk = ((PortableJukeboxItem)portable_jukebox_item.getItem()).getPlayingDisk(portable_jukebox_item);
                if (disk != null)
                {
//                    SoundUtils.playRecordAt(disk, player);
//					if (mc.isMultiplayerWorld()) Net
					NetworkHandler.sendToServer(new SongControlPacketC2S(player, disk, SongControlPacketS2C.SongAction.START_NEW));
                    PortableJukebox.LOGGER.debug("Started song");
                }
            }
            else if (SoundUtils.playing())
            {
//                SoundUtils.pause();
				NetworkHandler.sendToServer(new SongControlPacketC2S(player, SongControlPacketS2C.SongAction.PAUSE));
                PortableJukebox.LOGGER.debug("Paused song");
            }
            else
            {
//                SoundUtils.unpause();
				NetworkHandler.sendToServer(new SongControlPacketC2S(player, SongControlPacketS2C.SongAction.RESUME));
                PortableJukebox.LOGGER.debug("Unpaused song");
            }
        }
        else if (button.id == 1 && button.enabled)
        {
            PortableJukebox.LOGGER.debug("Stopping track");
			NetworkHandler.sendToServer(new SongControlPacketC2S(player, SongControlPacketS2C.SongAction.STOP));
//            SoundUtils.stop();
        }
        super.buttonClicked(button);
    }

    @Override
    public void render(int mx, int my, float partialTick)
    {
		super.render(mx, my, partialTick);
		if (!SoundUtils.playing())
        {
			buttons.get(0).displayString = "|>";
		} else
        {
			buttons.get(0).displayString = "||";
		}
		// TODO: there has to be a better way (referring to started_noupdate)
		buttons.get(1).enabled = SoundUtils.started_noupdate();
    }

    // private ItemDiscMusic disc;
//    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture("/assets/portable_jukebox/textures/gui/container/portable_jukebox_screen.png").bind();
        this.drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize/*, 0.006, 0.006*/);
    }
}
