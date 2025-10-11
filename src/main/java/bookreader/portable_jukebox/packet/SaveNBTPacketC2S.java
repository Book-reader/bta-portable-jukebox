package bookreader.portable_jukebox.packet;

import javax.annotation.Nonnull;

import com.mojang.nbt.tags.CompoundTag;

import bookreader.portable_jukebox.PortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class SaveNBTPacketC2S implements NetworkMessage {
    CompoundTag nbt;

    public SaveNBTPacketC2S(){}

    public SaveNBTPacketC2S(CompoundTag nbt)
    {
        this.nbt = nbt;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void encodeToUniversalPacket(@Nonnull UniversalPacket packet)
    {
        PortableJukebox.LOGGER.info("Encoding packet (SaveNBTPacketC2S)");
        assert this.nbt != null;
        packet.writeCompoundTag(nbt);
    }

    @Override
    @Environment(EnvType.SERVER)
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket packet)
    {
        PortableJukebox.LOGGER.info("Decoding packet (SaveNBTPacketC2S)");
        this.nbt = packet.readCompoundTag();
        assert this.nbt != null;
    }

    @Override
    public void handle(NetworkContext context)
    {
        PortableJukebox.LOGGER.info("Handling packet (SaveNBTPacketC2S)");
        assert this.nbt != null;
        Player player = context.player;
        player.getHeldItem().setData(nbt);
    }
}
