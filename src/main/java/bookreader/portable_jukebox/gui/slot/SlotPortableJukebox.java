package bookreader.portable_jukebox.gui.slot;

import bookreader.portable_jukebox.PortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

// @Environment(EnvType.CLIENT)
public class SlotPortableJukebox extends Slot {

   public SlotPortableJukebox(Container container, int index, int x, int y) {
      super(container, index, x, y);
   }

   @Override
   public boolean mayPlace(ItemStack item_stack)
   {
      return item_stack.getItem() instanceof ItemDiscMusic && item_stack.stackSize == 1;
   }
}