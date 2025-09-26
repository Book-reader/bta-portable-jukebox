package bookreader.portable_jukebox.gui.screen;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenPortableJukebox extends ScreenContainerAbstract {
    public ScreenPortableJukebox(ContainerInventory inventory, ItemStack portable_jukebox_item) {
        super(new MenuPortableJukebox(inventory, portable_jukebox_item));
    }
    
    // public void init()
    // {
    //     this.buttons.clear();
    //     this.buttons.add(new ButtonElement(0, getX(), getY(), 20, 20, "|>")); // I18n.getInstance().translateKey("gui.achievements.button.done")
    // }

    protected void buttonClicked(ButtonElement button)
    {
        PortableJukebox.LOGGER.info("Button Pressed!");
        super.buttonClicked(button);
    }

    // private ItemDiscMusic disc;
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        this.mc.textureManager.loadTexture("/gui/container.png").bind();
        this.drawTexturedModalRect(getX(), getY(), 0, 0, this.xSize, this.ySize);
    }

    private int getX()
    {
        return (this.width - this.xSize) / 2;
    }

    private int getY()
    {
        return (this.height - this.ySize) / 2;
    }
}