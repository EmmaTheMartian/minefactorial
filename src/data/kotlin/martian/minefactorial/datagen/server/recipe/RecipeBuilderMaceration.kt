package martian.minefactorial.datagen.server.recipe

import martian.minefactorial.content.recipe.RecipeMaceration
import martian.minefactorial.foundation.item.ChancedItemStack
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.NonNullList
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import org.jetbrains.annotations.ApiStatus

class RecipeBuilderMaceration(
	val ingredient: Ingredient,
	val powerPerTick: Int,
	val duration: Int,
	var category: RecipeCategory = RecipeCategory.MISC,
	val results: NonNullList<ChancedItemStack> = NonNullList.create<ChancedItemStack>(),
	val criteria: MutableMap<String, Criterion<*>> = mutableMapOf(),
) : RecipeBuilder {
	fun RecipeBuilderMaceration.addResult(item: ItemLike, chance: Float = 1f): RecipeBuilderMaceration {
		this.results.add(ChancedItemStack(item.asItem().defaultInstance, chance))
		return this
	}

	fun RecipeBuilderMaceration.addResult(item: ItemStack, chance: Float = 1f): RecipeBuilderMaceration {
		this.results.add(ChancedItemStack(item, chance))
		return this
	}

	fun RecipeBuilderMaceration.addResult(chancedItemStack: ChancedItemStack): RecipeBuilderMaceration {
		this.results.add(chancedItemStack)
		return this
	}

	fun RecipeBuilderMaceration.category(category: RecipeCategory): RecipeBuilderMaceration {
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
			RecipeMaceration(powerPerTick, duration, ingredient, results),
			builder.build(id.withPrefix("recipes/" + this.category.folderName + "/"))
		);
	}

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilderMaceration {
		this.criteria[name] = criterion
		return this
	}

	@ApiStatus.Obsolete
	@Deprecated("Maceration recipes do not use this.")
	override fun group(groupName: String?) = this

	@ApiStatus.Obsolete
	@Deprecated("Maceration recipes do not use this.")
	override fun getResult() = Items.AIR

	companion object {
		infix fun RecipeBuilderMaceration.addResult(item: ItemLike) = this.addResult(item)
		infix fun RecipeBuilderMaceration.addResult(item: ItemStack) = this.addResult(item)
	}
}
