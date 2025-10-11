package bookreader.portable_jukebox.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.gui.container.ContainerPortableJukebox;
import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.OpenGuiPacketS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerOpen;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuContainer;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import turniplabs.halplibe.helper.network.NetworkHandler;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixin extends Player implements DisplayPortableJukeboxScreen, ContainerListener {
    public PlayerServerMixin(World world)
	{
        super(world);
    }

    @Shadow
    private int currentWindowId = 0;
    @Shadow
    private void getNextWindowId() {}

    public void bta_portable_jukebox$displayPortableJukeboxScreen(ItemStack stack)
    {
        PortableJukebox.LOGGER.info("displayPortableJukeboxScreen (Server)");
        this.getNextWindowId();
        NetworkHandler.sendToPlayer(this, new OpenGuiPacketS2C(stack, this.currentWindowId));
        this.craftingInventory.onCraftGuiClosed(this);
        this.craftingInventory = new MenuPortableJukebox(this.inventory, stack);
        this.craftingInventory.containerId = this.currentWindowId;
        this.craftingInventory.addSlotListener(this);
	}

    @Inject(method = "updateInventorySlot(Lnet/minecraft/core/player/inventory/menu/MenuAbstract;ILnet/minecraft/core/item/ItemStack;)V", at = @At("HEAD"))
    public void updateInventorySlot(MenuAbstract container, int i, ItemStack itemstack, CallbackInfo info)
	{
		PortableJukebox.LOGGER.error("Updating inventory slot!");
	}
}
