package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.recipe.RecipeMaceration;
import martian.minefactorial.content.recipe.RecipeMeatPacking;
import martian.minefactorial.content.recipe.RecipeRanching;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class MFRecipeSerializers {
	private MFRecipeSerializers() { }

	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Minefactorial.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeMaceration>> MACERATION =
			REGISTRY.register("maceration", () -> RecipeMaceration.SERIALIZER);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeMeatPacking>> MEAT_PACKING =
			REGISTRY.register("meat_packing", () -> RecipeMeatPacking.SERIALIZER);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeRanching>> RANCHING =
			REGISTRY.register("ranching", () -> RecipeRanching.SERIALIZER);
}
