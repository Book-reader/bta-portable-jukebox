package bookreader.portable_jukebox.gui.container;

import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.jetbrains.annotations.Nullable;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.OpenGuiPacketS2C;
import bookreader.portable_jukebox.packet.SaveNBTPacketC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

// @Environment(EnvType.CLIENT)
public class ContainerPortableJukebox implements Container {
    private final ItemStack portable_jukebox_item;
	ItemStack[] storage;
    // private static final int DISK_STORAGE = 10;

    public ContainerPortableJukebox(ItemStack portable_jukebox_item)
    {
        assert portable_jukebox_item != null && portable_jukebox_item.getItem() instanceof PortableJukeboxItem;
        this.portable_jukebox_item = portable_jukebox_item;
        this.storage = PortableJukeboxItem.readNbt(this.portable_jukebox_item);//new ItemStack[DISK_STORAGE];
        // readNbt();
    }

    @Override
    public int getContainerSize() {
        return storage.length;
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
//		PortableJukebox.LOGGER.info("get item");
		return storage[i];
	}

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public String getNameTranslationKey() {
        PortableJukebox.LOGGER.info("TODO: PortableJukeboxContainer.getNameTranslationKey");
        return "container.portable_jukebox.TODO";
    }

    @Override
    public @Nullable ItemStack removeItem(int i, int j)
    {
//		PortableJukebox.LOGGER.info("Removing item from PortableJukebox");
        assert j == getMaxStackSize();
        if (storage[i] != null)
        {
            ItemStack s = storage[i];
            storage[i] = null;
            this.setChanged();
            return s;
        }
        return null;
    }

    @Override
    public void setChanged() {
//		PortableJukebox.LOGGER.info("setChanged");
		writeNbt();
		if (!EnvironmentHelper.isServerEnvironment())
		{
			if (storage[0] == null || !storage[0].getItem().equals(SoundUtils.currentRecord()))
			{
				SoundUtils.stop();
			}
			if (!EnvironmentHelper.isSinglePlayer()) NetworkHandler.sendToServer(new SaveNBTPacketC2S(portable_jukebox_item.getData()));
		}
//		if (EnvironmentHelper.isSinglePlayer()) writeNbt();
//        else if (EnvironmentHelper.isClientWorld())
//        {
//            NetworkHandler.sendToServer(new SaveNBTPacketC2S(portable_jukebox_item.getData()));
//        }
//        else
//        {
//            PortableJukebox.LOGGER.info("setChanged (SERVER)");
//        }
    }

    @Override
    public void setItem(int i, @Nullable ItemStack item)
    {
//		PortableJukebox.LOGGER.info("Setting item in PortableJukebox");
        assert item == null || item.getItem() instanceof ItemDiscMusic;
        storage[i] = item;
        this.setChanged();
    }

    @Override
    public void sortContainer() {
		InventorySorter.sortInventory(this.storage, 1, this.storage.length);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getHeldItem() != null && player.getHeldItem().equals(portable_jukebox_item);
    }

    void writeNbt()
    {
        CompoundTag itemstack_nbt = portable_jukebox_item.getData();
        CompoundTag items = new CompoundTag();
        for (int i = 0; i < storage.length; i++) if (storage[i] != null)
        {
            CompoundTag new_nbt = new CompoundTag();
            storage[i].writeToNBT(new_nbt);
            items.put(Integer.toString(i), new_nbt);
        }
        itemstack_nbt.put("Disks", items);
        portable_jukebox_item.setData(itemstack_nbt);
    }

}
