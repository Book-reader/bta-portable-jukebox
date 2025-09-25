package bookreader.portable_jukebox.gui.screen;

import org.lwjgl.opengl.GL11;

import bookreader.portable_jukebox.gui.container.ContainerPortableJukebox;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuFurnace;
import net.minecraft.core.player.inventory.slot.Slot;

@Environment(EnvType.CLIENT)
public class ScreenPortableJukebox extends ScreenContainerAbstract {
    public ScreenPortableJukebox(ContainerInventory inventory, ItemStack portable_jukebox_item) {
        super(new MenuPortableJukebox(inventory, portable_jukebox_item));
    }

    // private ItemDiscMusic disc;
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        this.mc.textureManager.loadTexture("/gui/container.png").bind();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}