package bookreader.portable_jukebox;

import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.OpenGuiPacketS2C;
import bookreader.portable_jukebox.packet.SaveNBTPacketC2S;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class PortableJukebox implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, DedicatedServerModInitializer {
	public static final String MOD_ID = "portable_jukebox";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Item PORTABLE_JUKEBOX;
    @Override
    public void onInitialize() {
        LOGGER.info("portable_jukebox initialized.");
		NetworkHandler.registerNetworkMessage(() -> new OpenGuiPacketS2C());
		NetworkHandler.registerNetworkMessage(() -> new SaveNBTPacketC2S());
		// PacketContainerSetContent
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {
		PORTABLE_JUKEBOX = new ItemBuilder(MOD_ID).build(new PortableJukeboxItem());
		// NetworkHandler.sendToPlayer(null, new OpenGuiPacketC2S());
	}

	@Override
	public void afterGameStart() {

	}

	public static String makeItemNamespace(String item_name)
	{
		return PortableJukebox.MOD_ID + ":item/" + item_name;
	}

	@Override
	public void onInitializeServer() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onInitializeServer'");
	}
}
