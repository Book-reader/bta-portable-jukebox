package bookreader.portable_jukebox.gui.slot;

import bookreader.portable_jukebox.PortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

@Environment(EnvType.CLIENT)
public class SlotPortableJukebox extends Slot {

    public SlotPortableJukebox(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    public boolean mayPlace(ItemStack item_stack)
    {
        return item_stack.getItem() instanceof ItemDiscMusic && item_stack.stackSize == 1;
    }

    // public void onTake(ItemStack itemstack)
    // {
    //     PortableJukebox.LOGGER.info("TODO: stop music when removing disk from first slot");
    //     super.onTake(itemstack);
    // }
}

/*public class SlotFurnace extends Slot {
   private Player thePlayer;

   public SlotFurnace(Player player, Container container, int index, int x, int y) {
      super(container, index, x, y);
      this.thePlayer = player;
   }

   public boolean mayPlace(ItemStack itemstack) {
      return false;
   }

   public void onTake(ItemStack itemstack) {
      itemstack.onCrafting(this.thePlayer.world, this.thePlayer);
      if (itemstack.itemID == Items.INGOT_IRON.id) {
         this.thePlayer.addStat(Achievements.ACQUIRE_IRON, 1);
      }

      if (itemstack.itemID == Items.FOOD_FISH_COOKED.id) {
         this.thePlayer.addStat(Achievements.COOK_FISH, 1);
      }

      super.onTake(itemstack);
   }

   public boolean enableDragAndPickup() {
      return false;
   }

   public boolean allowItemInteraction() {
      return false;
   }
}
 */