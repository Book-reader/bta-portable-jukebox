package bookreader.portable_jukebox.item;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.locks.Lock;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
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
	public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
		if (!world.isClientSide) {
			try
			{
				Field mc_field = PlayerLocal.class.getDeclaredField("mc");
				mc_field.setAccessible(true);
				Minecraft mc = (Minecraft)mc_field.get((PlayerLocal)player);

				if (player.isSneaking())
				{
					mc.displayScreen(new ScreenPortableJukebox(player.inventory, itemstack));
					return itemstack;
				}
				ItemDiscMusic record = getDisk(itemstack);
				if (record == null) return itemstack;

				SoundUtils.playRecordAt(record, player);
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
