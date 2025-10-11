package bookreader.portable_jukebox.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class SongControlPacketC2S implements NetworkMessage
{
    private String player_username;
	private int disc_id;
	SongControlPacketS2C.SongAction song_action;

    public SongControlPacketC2S(){}

	public SongControlPacketC2S(Player player, SongControlPacketS2C.SongAction action)
	{
		this.player_username = player.username;
		this.song_action = action;
	}

    public SongControlPacketC2S(Player player, ItemDiscMusic disc, SongControlPacketS2C.SongAction action)
    {
        this.player_username = player.username;
		this.song_action = action;
		this.disc_id = disc.id;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void encodeToUniversalPacket(@Nonnull UniversalPacket packet)
    {
		packet.writeString(player_username);
		packet.writeInt(song_action.ordinal());
		if (song_action == SongControlPacketS2C.SongAction.START_NEW)
		{
			packet.writeInt(disc_id);
		}
    }

    @Override
    @Environment(EnvType.SERVER)
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket packet)
    {
		player_username = packet.readString();
		song_action = SongControlPacketS2C.SongAction.values()[packet.readInt()];
		if (song_action == SongControlPacketS2C.SongAction.START_NEW)
		{
			disc_id = packet.readInt();
		}
    }

    @Override
	@Environment(EnvType.SERVER)
    public void handle(NetworkContext context)
    {
		NetworkHandler.sendToAllPlayers(new SongControlPacketS2C(player_username, disc_id, song_action));
    }
}
