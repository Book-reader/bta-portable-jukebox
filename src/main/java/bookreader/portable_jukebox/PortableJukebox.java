package bookreader.portable_jukebox;

import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.util.collection.NamespaceID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class PortableJukebox implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
	public static final String MOD_ID = "portable_jukebox";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("portable_jukebox initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {
		new ItemBuilder(MOD_ID).build(new PortableJukeboxItem());
	}

	@Override
	public void afterGameStart() {

	}

	public static String makeItemNamespace(String item_name)
	{
		return PortableJukebox.MOD_ID + ":" + item_name;
	}
}
