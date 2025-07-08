package martian.minefactorial.datagen.server.recipe

import martian.minefactorial.content.recipe.RecipeRanching
import martian.minefactorial.api.entity.EntityIngredient
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import org.jetbrains.annotations.ApiStatus
import top.girlkisser.lazuli.api.fluid.ChancedFluidStack
import top.girlkisser.lazuli.api.item.ChancedItemStack
import java.util.*

class RecipeBuilderRanching(
	val ingredient: EntityIngredient,
	var outputItem: Optional<ChancedItemStack> = Optional.empty(),
	var outputFluid: Optional<ChancedFluidStack> = Optional.empty(),
	var category: RecipeCategory = RecipeCategory.MISC,
	val criteria: MutableMap<String, Criterion<*>> = mutableMapOf(),
) : RecipeBuilder {
	fun RecipeBuilderRanching.setResultItem(item: ItemLike, chance: Float = 1f): RecipeBuilderRanching {
		this.outputItem = Optional.of(ChancedItemStack(item, chance))
		return this
	}

	fun RecipeBuilderRanching.setResultItem(item: ItemStack, chance: Float = 1f): RecipeBuilderRanching {
		this.outputItem = Optional.of(ChancedItemStack(item, chance))
		return this
	}

	fun RecipeBuilderRanching.setResultItem(stack: ChancedItemStack): RecipeBuilderRanching {
		this.outputItem = Optional.of(stack)
		return this
	}

	fun RecipeBuilderRanching.setResultFluid(fluid: Fluid, amount: Int, chance: Float = 1f): RecipeBuilderRanching {
		this.outputFluid = Optional.of(ChancedFluidStack(fluid, amount, chance))
		return this
	}

	fun RecipeBuilderRanching.setResultFluid(fluid: FluidStack, chance: Float = 1f): RecipeBuilderRanching {
		this.outputFluid = Optional.of(ChancedFluidStack(fluid, chance))
		return this
	}

	fun RecipeBuilderRanching.setResultFluid(stack: ChancedFluidStack): RecipeBuilderRanching {
		this.outputFluid = Optional.of(stack)
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
			RecipeRanching(ingredient, outputItem, outputFluid),
			builder.build(id.withPrefix("recipes/" + this.category.folderName + "/"))
		)
	}

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		this.criteria[name] = criterion
		return this
	}

	@ApiStatus.Obsolete
	@Deprecated("Maceration recipes do not use this.")
	override fun group(groupName: String?) = this

	@ApiStatus.Obsolete
	@Deprecated("Maceration recipes do not use this.")
	override fun getResult() = Items.AIR
}