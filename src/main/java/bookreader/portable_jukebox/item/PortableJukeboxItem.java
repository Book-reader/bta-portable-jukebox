package bookreader.portable_jukebox.item;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Lock;

import bookreader.portable_jukebox.PortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.SoundCategoryHelper;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import paulscode.sound.SoundSystem;

public class PortableJukeboxItem extends Item {
	public PortableJukeboxItem() {
		super("item.portable_jukebox", PortableJukebox.makeItemNamespace("portable_jukebox"), 29835);
		this.setMaxStackSize(1);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (!world.isClientSide) {
			try
			{
				ItemDiscMusic record = (ItemDiscMusic)Items.RECORD_DOG;
				Field mc_field = PlayerLocal.class.getDeclaredField("mc");
				mc_field.setAccessible(true);
				Minecraft mc = (Minecraft)mc_field.get((PlayerLocal)entityplayer);

				Field sndManager = Minecraft.class.getDeclaredField("sndManager");
				SoundEngine end = (SoundEngine)sndManager.get(mc);

				Field options = SoundEngine.class.getDeclaredField("options");
				options.setAccessible(true);

				Field lock = SoundEngine.class.getDeclaredField("lock");
				lock.setAccessible(true);

				SoundSystem soundSystem = SoundEngine.getSoundSystem();
				SoundEntry record_sound = SoundRepository.SOUNDS.getSoundEntry(record.recordName);
				try
				{
					((Lock)lock.get(null)).lock();
					if (soundSystem.playing(SoundEngine.BG_MUSIC)) soundSystem.stop(SoundEngine.BG_MUSIC);
					soundSystem.backgroundMusic(SoundEngine.BG_MUSIC, record_sound.getURL(), record_sound.name, false);
					soundSystem.setPitch(SoundEngine.BG_MUSIC, record_sound.pitch);
					soundSystem.setVolume(SoundEngine.BG_MUSIC, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, (GameSettings)options.get(end)) * record_sound.volume);
					soundSystem.play(SoundEngine.BG_MUSIC);
				} finally
				{
					((Lock)lock.get(null)).unlock();
				}

				Field hudIngame = Minecraft.class.getDeclaredField("hudIngame");
				if (record.recordAuthor != null)
				{
					((HudIngame) hudIngame.get(mc)).setRecordPlayingMessage(record.recordAuthor + " - " + I18n.getInstance().translateKey(record.recordName));
				}
				else
				{
					((HudIngame) hudIngame.get(mc)).setRecordPlayingMessage(I18n.getInstance().translateKey(record.getKey()));
				}
				PortableJukebox.LOGGER.info("Music should be playing");
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
        return itemstack;
    }

}
