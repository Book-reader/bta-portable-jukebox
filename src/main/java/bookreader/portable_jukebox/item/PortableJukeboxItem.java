package bookreader.portable_jukebox.item;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Lock;

import bookreader.portable_jukebox.PortableJukebox;
import net.betterthanadventure.sound.SourceLWJGL3OpenAL;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.HudIngameDoom;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.SoundCategoryHelper;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.entity.player.Player;
// import bookreader.portable_jukebox.mixin.IHearEverything;
// import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
// import net.minecraft.core.item.ItemRecord;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.sound.SoundTypes;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.phys.Vec3;
// import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.LevelListener;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import paulscode.sound.SoundSystem;
import paulscode.sound.Source;

public class PortableJukeboxItem extends Item {
	public PortableJukeboxItem() {
		super("portable_jukebox", PortableJukebox.makeItemNamespace("portable_jukebox"), 29835);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (!world.isClientSide) {
			try
			{
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
				SoundEntry dog = SoundRepository.SOUNDS.getSoundEntry("record.dog");
				try
				{
					((Lock)lock.get(null)).lock();
					if (soundSystem.playing(SoundEngine.BG_MUSIC)) soundSystem.stop(SoundEngine.BG_MUSIC);
					soundSystem.backgroundMusic(SoundEngine.BG_MUSIC, dog.getURL(), dog.name, false);
					soundSystem.setPitch(SoundEngine.BG_MUSIC, dog.pitch);
					soundSystem.setVolume(SoundEngine.BG_MUSIC, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, (GameSettings)options.get(end)) * dog.volume);
					soundSystem.play(SoundEngine.BG_MUSIC);
				} finally
				{
					((Lock)lock.get(null)).unlock();
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
