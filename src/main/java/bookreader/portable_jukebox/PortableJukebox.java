package bookreader.portable_jukebox;

import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class PortableJukebox implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, ModelEntrypoint {
	public static final String MOD_ID = "portable_jukebox";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static Item PORTABLE_JUKEBOX;
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
		PORTABLE_JUKEBOX = new ItemBuilder(MOD_ID).build(new PortableJukeboxItem());
	}

	@Override
	public void afterGameStart() {

	}

	
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {}
	
	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		ModelHelper.setItemModel(PORTABLE_JUKEBOX, ()->new ItemModelStandard(PORTABLE_JUKEBOX, null).setIcon(makeItemNamespace("portable_jukebox")));
	}
	
	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {}
	
	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {}
	
	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {}

	public static String makeItemNamespace(String item_name)
	{
		return PortableJukebox.MOD_ID + ":item/" + item_name;
	}
}
