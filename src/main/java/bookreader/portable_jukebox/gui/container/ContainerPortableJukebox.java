package bookreader.portable_jukebox.gui.container;

import net.minecraft.core.player.inventory.InventorySorter;
import org.jetbrains.annotations.Nullable;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.SaveNBTPacketC2S;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ContainerPortableJukebox implements Container {
    private final ItemStack portable_jukebox_item;
	ItemStack[] storage;

    public ContainerPortableJukebox(ItemStack portable_jukebox_item)
    {
        assert portable_jukebox_item != null && portable_jukebox_item.getItem() instanceof PortableJukeboxItem;
        this.portable_jukebox_item = portable_jukebox_item;
        this.storage = PortableJukeboxItem.readNbt(this.portable_jukebox_item);
    }

    @Override
    public int getContainerSize() {
        return storage.length;
    }

    @Override
    public @Nullable ItemStack getItem(int i)
	{
		return storage[i];
	}

    @Override
    public int getMaxStackSize()
	{
        return 1;
    }

    @Override
    public String getNameTranslationKey()
	{
        PortableJukebox.LOGGER.info("TODO: PortableJukeboxContainer.getNameTranslationKey");
        return "container.portable_jukebox.TODO";
    }

    @Override
    public @Nullable ItemStack removeItem(int i, int j)
    {
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
    public void setChanged()
	{
		writeNbt();
		if (!EnvironmentHelper.isServerEnvironment())
		{
			if (storage[0] == null || storage[0].getItem().id != SoundUtils.currentRecord().id)
			{
				SoundUtils.stop();
			}
			if (!EnvironmentHelper.isSinglePlayer()) NetworkHandler.sendToServer(new SaveNBTPacketC2S(portable_jukebox_item.getData()));
		}
    }

    @Override
    public void setItem(int i, @Nullable ItemStack item)
    {
        assert item == null || item.getItem() instanceof ItemDiscMusic;
        storage[i] = item;
        this.setChanged();
    }

    @Override
    public void sortContainer()
	{
		InventorySorter.sortInventory(this.storage, 1, this.storage.length - 1);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getHeldItem() != null && ItemStack.areItemStacksEqual(player.getHeldItem(), portable_jukebox_item);
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
