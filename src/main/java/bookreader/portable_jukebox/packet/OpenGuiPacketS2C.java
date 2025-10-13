package bookreader.portable_jukebox.packet;

import javax.annotation.Nonnull;

import bookreader.portable_jukebox.gui.menu.MenuPortableJukebox;
import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.iface.DisplayPortableJukeboxScreen;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class OpenGuiPacketS2C implements NetworkMessage {
    ItemStack stack;
    int windowId;

    public OpenGuiPacketS2C(){}

    public OpenGuiPacketS2C(ItemStack stack, int windowId)
    {
        assert stack.getItem() instanceof PortableJukeboxItem;
        this.stack = stack;
        this.windowId = windowId;
    }

    @Override
    @Environment(EnvType.SERVER)
    public void encodeToUniversalPacket(@Nonnull UniversalPacket packet)
    {
//        PortableJukebox.LOGGER.info("Encoding packet");
        assert this.stack != null;
        CompoundTag tag = new CompoundTag();
        stack.writeToNBT(tag);
        packet.writeCompoundTag(tag);
        packet.writeInt(windowId);
//        PortableJukebox.LOGGER.info("windowId = {}", windowId);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket packet)
    {
//        PortableJukebox.LOGGER.info("Decoding packet");
        this.stack = ItemStack.readItemStackFromNbt(packet.readCompoundTag());
        assert this.stack != null;
        this.windowId = packet.readInt();
//        PortableJukebox.LOGGER.info("recieved windowId = {}", windowId);
    }

    @Override
	@Environment(EnvType.CLIENT)
    public void handle(NetworkContext context)
    {
//        PortableJukebox.LOGGER.info("Handling packet (OpenGuiPacket), windowId = {}, stack = {}, player = {}", windowId, stack, Minecraft.getMinecraft().thePlayer.toString());
        assert this.stack != null;
        Player player = context.player;
        ((DisplayPortableJukeboxScreen)player).bta_portable_jukebox$displayPortableJukeboxScreen(stack);
//		player.craftingInventory.onCraftGuiClosed(player);
//		player.craftingInventory
//		player.craftingInventory = new MenuPortableJukebox(player.inventory, stack);
//		player.craftingInventory.containerId = windowId;
//		player.craftingInventory.addSlotListener(player);
        player.craftingInventory.containerId = windowId;
    }
}
