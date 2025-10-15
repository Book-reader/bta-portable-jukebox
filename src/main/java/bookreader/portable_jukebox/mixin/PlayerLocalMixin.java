package bookreader.portable_jukebox.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.item.ItemStack;

@Mixin(value = PlayerLocal.class, remap = false)
@Environment(EnvType.CLIENT)
public class PlayerLocalMixin implements DisplayPortableJukeboxScreen {
    @Shadow
    protected Minecraft mc;

    @Override
    public void bta_portable_jukebox$displayPortableJukeboxScreen(ItemStack stack)
    {
        this.mc.displayScreen(new ScreenPortableJukebox(((PlayerLocal)(Object)this).inventory, stack));
    }
}
