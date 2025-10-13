package bookreader.portable_jukebox.packet;

import bookreader.portable_jukebox.PortableJukebox;
import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.iface.PlayDiscFromPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.server.MinecraftServer;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

public class SongControlPacketS2C implements NetworkMessage
{
    private String player_username;
	private int disc_id;
	SongAction song_action;

    public SongControlPacketS2C(){}

//    public SongControlPacketS2C(Player player, ItemDiscMusic disc, SongAction action)
//    {
//        this.player_username = player.username;
//		this.song_action = action;
//		this.disc_id = disc.id;
//    }

	public SongControlPacketS2C(String player_username, int disc_id, SongAction action) {
		this.player_username = player_username;
		this.song_action = action;
		this.disc_id = disc_id;
	}

	@Override
	@Environment(EnvType.SERVER)
    public void encodeToUniversalPacket(@Nonnull UniversalPacket packet)
    {
		packet.writeString(player_username);
		packet.writeInt(song_action.ordinal());
		if (song_action == SongAction.START_NEW || song_action == SongAction.RESUME)
		{
			packet.writeInt(disc_id);
		}
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void decodeFromUniversalPacket(@Nonnull UniversalPacket packet)
    {
		player_username = packet.readString();
		song_action = SongAction.values()[packet.readInt()];
		if (song_action == SongAction.START_NEW || song_action == SongAction.RESUME)
		{
			disc_id = packet.readInt();
		}
    }

    @Override
	@Environment(EnvType.CLIENT)
    public void handle(NetworkContext context)
    {
		PortableJukebox.LOGGER.info("Handling packet (SongControlPacketS2C)");
		// TODO: just return if it's the same player
		if (player_username.equals(Minecraft.getMinecraft().thePlayer.username))
		{
			switch (song_action)
			{
				case START_NEW -> SoundUtils.playRecordAt((ItemDiscMusic) Item.getItem(disc_id), context.player);
				case PAUSE -> SoundUtils.pause();
				case RESUME -> SoundUtils.unpause();
				case STOP -> SoundUtils.stop();
			}
		}
		else
		{
			// TODO: handle dimensions
			PlayDiscFromPlayer snd = (PlayDiscFromPlayer) Minecraft.getMinecraft().sndManager;
			Player player = context.player.world.players.stream().filter((pl) -> pl.username.equals(player_username)).findFirst().get();
			switch (song_action) {
				case START_NEW -> snd.bta_portable_jukebox$playDiscFrom((ItemDiscMusic) Item.getItem(disc_id), player);
				case PAUSE, STOP -> snd.bta_portable_jukebox$pauseDiscFrom(player);
				case RESUME -> snd.bta_portable_jukebox$resumeDiscFrom((ItemDiscMusic) Item.getItem(disc_id), player);
			}
		}

    }

	public enum SongAction
	{
		START_NEW,
		PAUSE,
		RESUME,
		STOP,
	}
}
