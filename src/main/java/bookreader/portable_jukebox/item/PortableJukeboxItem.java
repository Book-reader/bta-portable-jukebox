package bookreader.portable_jukebox.item;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class PortableJukeboxItem extends Item {
	public static final int STORAGE_SIZE = 10;
	public static final int SCREEN_ID = 31;

	public PortableJukeboxItem() {
		super("item.portable_jukebox", PortableJukebox.makeItemNamespace("portable_jukebox"), 29835);
		this.setMaxStackSize(1);
	}

	@Override
	// @Environment(EnvType.CLIENT)
	public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
		PortableJukebox.LOGGER.info("is client: " + world.isClientSide);
		// if (EnvironmentHelper.isSinglePlayer())
		// {
		// 	PortableJukebox.LOGGER.info("A");
			// GuiManager.openGuiClient(player, itemstack);
			// Util.getMinecraft().displayScreen(new ScreenPortableJukebox(player.inventory, itemstack));
		// }
		// else if (EnvironmentHelper.isServerEnvironment())
		// {
		// 	PortableJukebox.LOGGER.info("B");
			// PortableJukebox.LOGGER.info("on server!");
		if (!world.isClientSide)
		{
			PortableJukebox.LOGGER.info("sending display screen message");
			((DisplayPortableJukeboxScreen)player).bta_portable_jukebox$displayPortableJukeboxScreen(itemstack);
//			 NetworkHandler.sendToPlayer(player, new OpenGuiPacketS2C(itemstack));
		}
        return itemstack;
    }

	public ItemDiscMusic getPlayingDisk(ItemStack stack)
	{
		CompoundTag disks = stack.getData().getCompound("Disks");
		if (disks == null) return null;
		ItemStack s = ItemStack.readItemStackFromNbt(disks.getCompound("0"));
		if (s == null) return null;
		assert s.getItem() instanceof ItemDiscMusic;
		return (ItemDiscMusic)s.getItem();
	}

	public static ItemStack[] readNbt(ItemStack portable_jukebox_item)
    {
		assert portable_jukebox_item != null;
		assert portable_jukebox_item.getItem() instanceof PortableJukeboxItem;

		ItemStack[] disk_storage = new ItemStack[STORAGE_SIZE];
        CompoundTag disks = portable_jukebox_item.getData().getCompound("Disks");
        for (int i = 0; i < disk_storage.length; i++)
        {
            CompoundTag disk = disks.getCompound(Integer.toString(i));
            if (disk != null)
            {
                disk_storage[i] = ItemStack.readItemStackFromNbt(disk);
				assert disk_storage[i].getItem() instanceof ItemDiscMusic;
            }
        }
		return disk_storage;
    }
}
