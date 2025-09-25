package bookreader.portable_jukebox.item;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
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
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
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
				Field mc_field = PlayerLocal.class.getDeclaredField("mc");
				mc_field.setAccessible(true);
				Minecraft mc = (Minecraft)mc_field.get((PlayerLocal)entityplayer);

				if (entityplayer.isSneaking())
				{
					mc.displayScreen(new ScreenPortableJukebox(entityplayer.inventory, itemstack));
					return itemstack;
				}
				ItemDiscMusic record = getDisk(itemstack);
				if (record == null) return itemstack;


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
					soundSystem.setVolume(SoundEngine.BG_MUSIC, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, (GameSettings)options.get(mc.sndManager)) * record_sound.volume);
					soundSystem.play(SoundEngine.BG_MUSIC);
				} finally
				{
					((Lock)lock.get(null)).unlock();
				}

				if (record.recordAuthor != null)
				{
					mc.hudIngame.setRecordPlayingMessage(record.recordAuthor + " - " + I18n.getInstance().translateKey(record.recordName));
				}
				else
				{
					mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(record.getKey()));
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

	ItemDiscMusic getDisk(ItemStack stack)
	{
		CompoundTag disks = stack.getData().getCompound("Disks");
		if (disks == null) return null;
		ItemStack s = ItemStack.readItemStackFromNbt(disks.getCompound("0"));
		if (s == null) return null;
		assert s.getItem() instanceof ItemDiscMusic;
		return (ItemDiscMusic)s.getItem();
	}
}
