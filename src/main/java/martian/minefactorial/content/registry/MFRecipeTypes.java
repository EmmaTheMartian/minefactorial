package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.recipe.RecipeMaceration;
import martian.minefactorial.content.recipe.RecipeMeatPacking;
import martian.minefactorial.content.recipe.RecipeRanching;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MFRecipeTypes {
	private MFRecipeTypes() { }

	public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, Minefactorial.MODID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeMaceration>> MACERATION =
			REGISTRY.register("maceration", () -> RecipeMaceration.TYPE);

	public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeMeatPacking>> MEAT_PACKING =
			REGISTRY.register("meat_packing", () -> RecipeMeatPacking.TYPE);

	public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeRanching>> RANCHING =
			REGISTRY.register("ranching", () -> RecipeRanching.TYPE);
}
