package bookreader.portable_jukebox.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.gui.container.ContainerPortableJukebox;
import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import bookreader.portable_jukebox.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;

@Mixin(value = PlayerLocal.class, remap = false)
@Environment(EnvType.CLIENT)
public class PlayerLocalMixin implements DisplayPortableJukeboxScreen {
    @Shadow
    protected Minecraft mc;

    @Override
    public void bta_portable_jukebox$displayPortableJukeboxScreen(ItemStack stack)
    {
        PortableJukebox.LOGGER.info("displayPortableJukeboxScreen (Client)");
        this.mc.displayScreen(new ScreenPortableJukebox(((PlayerLocal)(Object)this).inventory, stack));
    }
}
