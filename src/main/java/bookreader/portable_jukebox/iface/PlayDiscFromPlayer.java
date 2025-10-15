package bookreader.portable_jukebox.iface;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;

public interface PlayDiscFromPlayer {
	void bta_portable_jukebox$playDiscFrom(ItemDiscMusic record, Player player);
	void bta_portable_jukebox$pauseDiscFrom(Player player);
	void bta_portable_jukebox$resumeDiscFrom(Player player);
}
