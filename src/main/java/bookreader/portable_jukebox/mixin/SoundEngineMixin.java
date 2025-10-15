package bookreader.portable_jukebox.mixin;

import java.util.*;
import java.util.concurrent.locks.Lock;

import javax.annotation.Nullable;

import bookreader.portable_jukebox.iface.PlayDiscFromPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
// TODO: better handle the specific cases instead of mixining everything
public abstract class SoundEngineMixin implements PlayDiscFromPlayer
{
    @Shadow
    private static SoundSystem soundSystem;
    @Shadow
    private @Nullable GameSettings options;
    @Shadow
    private static boolean loaded;
	@Shadow
	private Minecraft mc;
	@Shadow
	public void playSoundWithIdAtPos(SoundEntry entry, SoundCategory category, float x, float y, float z, float volume, float pitch, String soundID) {throw new AssertionError();}

	@Shadow
	protected abstract boolean isLoaded();
	@Unique
	Map<Player, String> listeningDiscsFrom = new HashMap<>();

	@Override
	public void bta_portable_jukebox$playDiscFrom(ItemDiscMusic record, Player player)
	{
		String category_name;
		if (listeningDiscsFrom.containsKey(player))
		{
			category_name = listeningDiscsFrom.get(player);
			if (soundSystem.playing(category_name)) soundSystem.stop(category_name);
		}
		else
		{
			category_name = SoundUtils.SOUND_CATEGORY + player.username;
			listeningDiscsFrom.put(player, category_name);
		}
		SoundEntry record_sound = SoundRepository.SOUNDS.getSoundEntry(record.recordName);
		assert record_sound != null;
		if (soundSystem.playing(SoundEngine.BG_MUSIC)) soundSystem.stop(SoundEngine.BG_MUSIC);
		this.playSoundWithIdAtPos(record_sound, SoundCategory.MUSIC, (float)player.x, (float)player.y, (float)player.z, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, mc.gameSettings) * record_sound.volume, record_sound.pitch, category_name);

		if (record.recordAuthor != null)
		{
			mc.hudIngame.setRecordPlayingMessage(record.recordAuthor + " - " + I18n.getInstance().translateKey(record.recordName));
		}
		else
		{
			mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(record.getKey()));
		}
	}

	@Override
	public void bta_portable_jukebox$pauseDiscFrom(Player player) {
		if (listeningDiscsFrom != null && listeningDiscsFrom.containsKey(player))
		{
			soundSystem.stop(listeningDiscsFrom.get(player));
		}
	}

	@Override
	public void bta_portable_jukebox$resumeDiscFrom(Player player) {
		if (listeningDiscsFrom.containsKey(player))
		{
			soundSystem.play(listeningDiscsFrom.get(player));
		}
	}

	@Inject(method = "updateListener(Lnet/minecraft/core/entity/Mob;F)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/Lock;unlock()V"))
	void beforeUpdateListenerLock(Mob listener_player, float partialTick, CallbackInfo ci)
	{
		for (Map.Entry<Player, String> entry : listeningDiscsFrom.entrySet())
		{
			String category = entry.getValue();
			if (soundSystem.playing(category))
			{
				Player player = entry.getKey();
				soundSystem.setPosition(category, (float) player.x, (float) player.y, (float) player.z);
			}
		}
	}

    @WrapOperation(method = "tick()V", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;playing(Ljava/lang/String;)Z"))
    public boolean soundSystemPlaying(SoundSystem system, String name, Operation<Boolean> original)
    {
        if (Objects.equals(name, SoundEngine.BG_MUSIC)) return original.call(system, name) || system.playing(SoundUtils.SOUND_CATEGORY);
        else return original.call(system, name);
    }

    @Inject(method = "stopMusic()V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/Lock;unlock()V"))
    public void soundSystemStop(CallbackInfo ci)
    {
		if (this.isLoaded())
		{
			soundSystem.stop(SoundUtils.SOUND_CATEGORY);
			for (String disc_cat : listeningDiscsFrom.values())
			{
				soundSystem.stop(disc_cat);
			}
		}
    }

	@Inject(method = "setMuted(Z)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/Lock;unlock()V"))
	public void onSetMuted(boolean muted, CallbackInfo ci)
	{
		if (!this.isLoaded()) return;
		if (muted) {
			soundSystem.setVolume(SoundUtils.SOUND_CATEGORY, 0.0F);
			for (String disc_cat : listeningDiscsFrom.values())
			{
				soundSystem.setVolume(disc_cat, 0.0f);
			}
		} else {
			soundSystem.setVolume(SoundUtils.SOUND_CATEGORY, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options));
			for (String disc_cat : listeningDiscsFrom.values())
			{
				soundSystem.setVolume(disc_cat, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options));
			}
		}
	}

    @Inject(method = "updateOptions()V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/Lock;unlock()V"))
    public void onUpdateOptions(CallbackInfo info)
    {
        if (soundSystem != null && loaded) {
            soundSystem.setVolume(SoundUtils.SOUND_CATEGORY, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options));
			for (String disc_cat : listeningDiscsFrom.values())
			{
				if (soundSystem.playing(disc_cat)) soundSystem.setVolume(disc_cat, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options));
			}
        }
    }
}
