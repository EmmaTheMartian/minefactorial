package martian.minefactorial.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import martian.minefactorial.content.recipe.RecipeMaceration;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import static martian.minefactorial.Minefactorial.id;

@EmiEntrypoint
public class MFEmiPlugin implements EmiPlugin {
	public static final ResourceLocation ICONS = id("textures/gui/emi/simplified_icons.png");

	public static final EmiTexture ENERGY_BAR = new EmiTexture(ICONS, 0, 64, 105, 4);
	public static final EmiTexture WORK_BAR = new EmiTexture(ICONS, 0, 68, 105, 4);
	public static final EmiTexture IDLE_BAR = new EmiTexture(ICONS, 0, 72, 105, 4);

	public static final EmiStack MACERATOR = EmiStack.of(MFBlocks.MACERATOR);
	public static final EmiRecipeCategory MACERATION = new EmiRecipeCategory(id("maceration"), MACERATOR, new EmiTexture(ICONS, 0, 0, 16, 16));

	@Override
	public void register(EmiRegistry registry) {
		RecipeManager rm = registry.getRecipeManager();

		registry.addCategory(MACERATION);
		registry.addWorkstation(MACERATION, MACERATOR);
		for (RecipeHolder<RecipeMaceration> recipe : rm.getAllRecipesFor(MFRecipeTypes.MACERATION.get())) {
			registry.addRecipe(new EmiRecipeMaceration(recipe));
		}
	}
}
