package bookreader.portable_jukebox.gui.container;

import org.jetbrains.annotations.Nullable;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;

@Environment(EnvType.CLIENT)
public class ContainerPortableJukebox implements Container {
    private final ItemStack portable_jukebox_item;
    ItemStack[] storage;
    private static final int DISK_STORAGE = 10;

    public ContainerPortableJukebox(ItemStack portable_jukebox_item)
    {
        assert portable_jukebox_item.getItem() instanceof PortableJukeboxItem;
        this.portable_jukebox_item = portable_jukebox_item;
        this.storage = new ItemStack[DISK_STORAGE];
        readNbt();
    }

    @Override
    public int getContainerSize() {
        return DISK_STORAGE;
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
        return storage[i];
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public String getNameTranslationKey() {
        PortableJukebox.LOGGER.info("TODO: PortableJukeboxContainer.getNameTranslationKey");
        return "portable_jukebox.TODO";
    }

    @Override
    public @Nullable ItemStack removeItem(int i, int j)
    {
        assert j == getMaxStackSize();
        if (storage[i] != null)
        {
            ItemStack s = storage[i];
            storage[i] = null;
            return s;
        }
        return null;
    }

    @Override
    public void setChanged() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setChanged'");
        writeNbt();
        PortableJukebox.LOGGER.info("TODO: PortableJukeboxContainer.setChanged");
    }

    @Override
    public void setItem(int i, @Nullable ItemStack item)
    {
        assert item == null || item.getItem() instanceof ItemDiscMusic;
        storage[i] = item;
    }

    @Override
    public void sortContainer() {
        PortableJukebox.LOGGER.info("TODO: PortableJukeboxContainer.sortContainer");
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getHeldItem().equals(portable_jukebox_item);
    }

    void readNbt()
    {
        CompoundTag disks = portable_jukebox_item.getData().getCompound("Disks");
        for (int i = 0; i < storage.length; i++)
        {
            CompoundTag disk = disks.getCompound(Integer.toString(i));
            if (disk != null)
            {
                storage[i] = ItemStack.readItemStackFromNbt(disk);
            }
        }
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
    }

}
