package bookreader.portable_jukebox.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.util.Util;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketContainerOpen;

// This is disabled
/*
@Mixin(value = PacketHandlerClient.class, remap = false)
public class PacketHandlerClientMixin {
    
    @Inject(method = "handleOpenWindow(Lnet/minecraft/core/net/packet/PacketContainerOpen;)V", at = @At("HEAD"), cancellable = true)
    public void handleOpenWindow(PacketContainerOpen containerOpenPacket, CallbackInfo info)
    {
        if (containerOpenPacket.inventoryType == PortableJukeboxItem.SCREEN_ID)
        {
            ((DisplayPortableJukeboxScreen)Util.getMinecraft().thePlayer).displayPortableJukeboxScreen(Util.getMinecraft().thePlayer.getHeldItem());
            Util.getMinecraft().thePlayer.craftingInventory.containerId = containerOpenPacket.windowId;
            info.cancel();
        }
    }
}
*/