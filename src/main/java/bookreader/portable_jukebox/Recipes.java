package bookreader.portable_jukebox;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

public class Recipes extends RecipeRegistry {
	public static final RecipeNamespace PORTABLE_JUKEBOX = new RecipeNamespace();

	public static void initRecipes() {
//		RecipeBuilder.Shaped(PortableJukebox.MOD_ID)
//			.setShape(
//				"rrr",
//				"rdr",
//				"rrr")
//			.addInput('r', yourItemHere) // Replace with your input item/block
//			.addInput('d', Item.foodAppleGold) // Example of another input item
//			.create("YourRecipeName", new PortableJukeboxItem().getDefaultStack()); // Replace with your output item/block
		RecipeBuilder.Shapeless(PortableJukebox.MOD_ID)
			.addInput(Blocks.JUKEBOX.getDefaultStack())
			.addInput("minecraft:chests")
			.create("portable_jukebox", PortableJukebox.PORTABLE_JUKEBOX.getDefaultStack());
	}

	public static void initNameSpaces() {
		final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
		PORTABLE_JUKEBOX.register("workbench", WORKBENCH);
		Registries.RECIPES.register(PortableJukebox.MOD_ID, PORTABLE_JUKEBOX);
	}
}
