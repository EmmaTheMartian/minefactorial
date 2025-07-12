package martian.minefactorial.datagen.server.recipe

import martian.minefactorial.content.recipe.RecipeMeatPacking
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import org.jetbrains.annotations.ApiStatus

class RecipeBuilderMeatPacking(
	val ingredient: FluidIngredient,
	val inputAmount: Int,
	val result: ItemStack,
	val powerPerTick: Int = 2,
	val duration: Int = 60,
	var category: RecipeCategory = RecipeCategory.MISC,
	val criteria: MutableMap<String, Criterion<*>> = mutableMapOf(),
) : RecipeBuilder {
	fun RecipeBuilderMeatPacking.category(category: RecipeCategory): RecipeBuilderMeatPacking {
		this.category = category
		return this
	}

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val builder = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)
		criteria.forEach(builder::addCriterion)
		recipeOutput.accept(
			id,
			RecipeMeatPacking(powerPerTick, duration, ingredient, inputAmount, result),
			builder.build(id.withPrefix("recipes/" + this.category.folderName + "/"))
		)
	}

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilderMeatPacking {
		this.criteria[name] = criterion
		return this
	}

	@ApiStatus.Obsolete
	@Deprecated("Meat packing recipes do not use this.")
	override fun group(groupName: String?) = this

	override fun getResult() = result.item
}
