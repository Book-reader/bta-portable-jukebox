package bookreader.portable_jukebox.packet;

import javax.annotation.Nonnull;

import com.mojang.nbt.tags.CompoundTag;

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
        assert this.nbt != null;
        packet.writeCompoundTag(nbt);
    }

    @Override
    @Environment(EnvType.SERVER)
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket packet)
    {
        this.nbt = packet.readCompoundTag();
        assert this.nbt != null;
    }

    @Override
    public void handle(NetworkContext context)
    {
        assert this.nbt != null;
        Player player = context.player;
		assert player.getHeldItem() != null;
        player.getHeldItem().setData(nbt);
    }
}
