package bookreader.portable_jukebox.gui.menu;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import bookreader.portable_jukebox.gui.container.ContainerPortableJukebox;
import bookreader.portable_jukebox.gui.slot.SlotPortableJukebox;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

public class MenuPortableJukebox extends MenuAbstract {
    private final @NotNull ItemStack portable_jukebox_item;
    private final ContainerPortableJukebox storage;

    public MenuPortableJukebox(ContainerInventory inventory, @NotNull ItemStack portable_jukebox_item) {
        this.portable_jukebox_item = portable_jukebox_item;
        storage = new ContainerPortableJukebox(portable_jukebox_item);
        this.addSlot(new SlotPortableJukebox(storage, 0, 8, 8));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new SlotPortableJukebox(storage, 1 + j + i * 3, (8 + 6 * 18) + j * 18, (84 - 18 * 3 - 4) + i * 18));
            }
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player player) {
        if (slot.index >= 0 && slot.index < storage.getContainerSize())
        {
            return this.getSlots(0, storage.getContainerSize(), false);
        }
        else if (slot.index >= storage.getContainerSize() && slot.index < ContainerInventory.playerMainInventorySize())
        {
            return this.getSlots(storage.getContainerSize(), ContainerInventory.playerMainInventorySize(), false);
        }
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player player)
	{
		if (this.getSlot(0).getItemStack() == null && slot.index > 0 && slot.index < this.storage.getContainerSize())
		{
			List<Integer> s = this.getSlots(0, 1, false);
			s.addAll(this.getSlots(this.storage.getContainerSize(), ContainerInventory.playerMainInventorySize(), true));
			return s;
		}
		if (slot.index == 0) return this.getSlots(1, this.storage.getContainerSize() + ContainerInventory.playerMainInventorySize() - 1, false);
		return slot.index < this.storage.getContainerSize() ? this.getSlots(this.storage.getContainerSize(), ContainerInventory.playerMainInventorySize(), true) : this.getSlots(0, this.storage.getContainerSize(), false);
    }

    @Override
    public boolean stillValid(Player player)
	{
		return portable_jukebox_item.getItem() instanceof PortableJukeboxItem && ItemStack.areItemStacksEqual(player.getHeldItem(), portable_jukebox_item);
	}
}
