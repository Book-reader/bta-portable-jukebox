package bookreader.portable_jukebox.mixin;

import org.spongepowered.asm.mixin.Mixin;

import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin implements DisplayPortableJukeboxScreen {

    @Override
    public void bta_portable_jukebox$displayPortableJukeboxScreen(ItemStack stack) {
        throw new UnsupportedOperationException("Unimplemented method 'displayPortableJukeboxScreen'");
    }

}
