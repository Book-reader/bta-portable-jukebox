package bookreader.portable_jukebox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundEngine;
import paulscode.sound.SoundSystem;
@Mixin(value = SoundEngine.class, remap = false)
@Environment(EnvType.CLIENT)
// TODO: better handle the specific cases instead of mixining everything
public class SoundEngineMixin
{
    @WrapOperation(method = /*{"tick()V", "playMusic(Lnet/minecraft/client/sound/SoundEntry;FFFFF)V", "stopMusic()V"}*/"*", at = @At(value = "INVOKE", target = "playing(Ljava/lang/String;)Z"))
    public boolean soundSystemPlaying(SoundSystem system, String name, Operation<Boolean> original)
    {
        if (name == SoundEngine.BG_MUSIC) return original.call(system, name) || system.playing(SoundUtils.SOUND_CATEGORY);
        else return original.call(system, name);
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "stop(Ljava/lang/String;)V"))
    public void soundSystemStop(SoundSystem system, String name, Operation<Void> original)
    {
        if (name == SoundEngine.BG_MUSIC) system.stop(SoundUtils.SOUND_CATEGORY);
        original.call(system, name);
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "setVolume(Ljava/lang/String;F)V"))
    public void soundSystemSetVolume(SoundSystem system, String name, float vol, Operation<Void> original)
    {
        if (name == SoundEngine.BG_MUSIC) system.setVolume(SoundUtils.SOUND_CATEGORY, vol);
        original.call(system, name, vol);
    }
}
