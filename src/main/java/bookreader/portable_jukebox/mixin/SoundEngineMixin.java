package bookreader.portable_jukebox.mixin;

import java.util.concurrent.locks.Lock;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.SoundCategoryHelper;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.core.sound.SoundCategory;
import paulscode.sound.SoundSystem;
@Mixin(value = SoundEngine.class, remap = false)
@Environment(EnvType.CLIENT)
// TODO: better handle the specific cases instead of mixining everything
public class SoundEngineMixin
{
    @Shadow
    private static SoundSystem soundSystem;
    @Shadow
    private @Nullable GameSettings options;
    @Shadow
    private static boolean loaded;

    @WrapOperation(method = {"tick()V"}, at = @At(value = "INVOKE", target = "playing(Ljava/lang/String;)Z"))
    public boolean soundSystemPlaying(SoundSystem system, String name, Operation<Boolean> original)
    {
        if (name == SoundEngine.BG_MUSIC) return original.call(system, name) || system.playing(SoundUtils.SOUND_CATEGORY);
        else return original.call(system, name);
    }

    @WrapOperation(method = {"stopMusic()V"}, at = @At(value = "INVOKE", target = "stop(Ljava/lang/String;)V"))
    public void soundSystemStop(SoundSystem system, String name, Operation<Void> original)
    {
        if (name == SoundEngine.BG_MUSIC && system.playing(SoundUtils.SOUND_CATEGORY))
        {
            PortableJukebox.LOGGER.info("Stopping music!");
            system.stop(SoundUtils.SOUND_CATEGORY);
        }
        if (system.playing(name)) original.call(system, name);
    }

    @WrapOperation(method = "setMuted(Z)V", at = @At(value = "INVOKE", target = "setVolume(Ljava/lang/String;F)V"))
    public void soundSystemSetVolume(SoundSystem system, String name, float vol, Operation<Void> original)
    {
        if (name == SoundEngine.BG_MUSIC) system.setVolume(SoundUtils.SOUND_CATEGORY, vol);
        original.call(system, name, vol);
    }

    @Inject(method = "updateOptions()V", at = @At(value = "INVOKE", target = "unlock()V"))
    public void onUpdateOptions(CallbackInfo info)
    {
        if (soundSystem != null && loaded) {
            soundSystem.setVolume(SoundUtils.SOUND_CATEGORY, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options));
        }
    }
}
