package bookreader.portable_jukebox;

import bookreader.portable_jukebox.item.PortableJukeboxItem;
import bookreader.portable_jukebox.packet.OpenGuiPacketS2C;
import bookreader.portable_jukebox.packet.SaveNBTPacketC2S;
import bookreader.portable_jukebox.packet.SongControlPacketC2S;
import bookreader.portable_jukebox.packet.SongControlPacketS2C;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.registry.Registries;
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
		LOGGER.info("{} initialized.", MOD_ID);
		NetworkHandler.registerNetworkMessage(OpenGuiPacketS2C::new);
		NetworkHandler.registerNetworkMessage(SaveNBTPacketC2S::new);
		NetworkHandler.registerNetworkMessage(SongControlPacketS2C::new);
		NetworkHandler.registerNetworkMessage(SongControlPacketC2S::new);
	}

	@Override
	public void onRecipesReady()
	{
		Recipes.initRecipes();
	}

	@Override
	public void initNamespaces()
	{
		Recipes.initNameSpaces();
	}

	@Override
	public void beforeGameStart()
	{
		PORTABLE_JUKEBOX = new ItemBuilder(MOD_ID).build(new PortableJukeboxItem());
	}

	@Override
	public void afterGameStart() {}

	public static String makeItemNamespace(String item_name)
	{
		return PortableJukebox.MOD_ID + ":item/" + item_name;
	}

	@Override
	public void onInitializeServer()
	{

	}
}
