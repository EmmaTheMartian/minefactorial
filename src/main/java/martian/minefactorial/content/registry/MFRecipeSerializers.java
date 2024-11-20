package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.recipe.RecipeMaceration;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MFRecipeSerializers {
	private MFRecipeSerializers() { }

	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Minefactorial.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RecipeMaceration>> MACERATION = REGISTRY.register("maceration", () -> RecipeMaceration.SERIALIZER);
}
