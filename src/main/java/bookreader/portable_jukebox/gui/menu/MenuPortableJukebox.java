package bookreader.portable_jukebox.gui.menu;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.gui.container.ContainerPortableJukebox;
import bookreader.portable_jukebox.gui.slot.SlotPortableJukebox;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuContainer;
import net.minecraft.core.player.inventory.slot.Slot;

@Environment(EnvType.CLIENT)
public class MenuPortableJukebox extends MenuAbstract {
    private final @NotNull ItemStack portable_jukebox_item;
    private final ContainerPortableJukebox storage;

    public MenuPortableJukebox(ContainerInventory inventory, ItemStack portable_jukebox_item) {
        this.portable_jukebox_item = portable_jukebox_item;
        storage = new ContainerPortableJukebox(portable_jukebox_item);

        for (int i = 0; i < storage.getContainerSize(); i++)
        {
            this.addSlot(new SlotPortableJukebox(storage, i, 20 + (i * 18), 20));
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
        // this.disk = disk;
        // this.addSlot(new Slot(new MenuContainer(playerContainer, container)));
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player player) {
        // if (i )
        // return this.getSlots(0, 100000000, false);
        PortableJukebox.LOGGER.info("TODO: MenuPortableJukebox.getMoveSlots");
        return null;
        // if (i < storage.getContainerSize())
        // {
        //     return this.getSlots(storage.getContainerSize(), player.inventory.getContainerSize(), false);
        // }
        // else
        // {
        //     return this.getSlots(0, storage.getContainerSize(), false);
        // }
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player player) {
        if (i < storage.getContainerSize())
        {
            return this.getSlots(storage.getContainerSize(), player.inventory.getContainerSize(), false);
        }
        else
        {
            return this.getSlots(0, storage.getContainerSize(), false);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return portable_jukebox_item.getItem() instanceof PortableJukeboxItem;
    }

}
