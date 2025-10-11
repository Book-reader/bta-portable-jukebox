package bookreader.portable_jukebox.mixin;

import bookreader.portable_jukebox.PortableJukebox;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(value = ContainerInventory.class, remap = false)
//public class ContainerInventoryMixin {
//	@Inject(method = "setChanged()V", at = @At("HEAD"))
//	void onSetChanged(CallbackInfo info)
//	{
//		PortableJukebox.LOGGER.info("Inventory setChanged");
//	}
//}
