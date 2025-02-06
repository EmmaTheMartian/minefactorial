package martian.minefactorial.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.minefactorial.foundation.entity.EntityIngredient;
import martian.minefactorial.foundation.fluid.ChancedFluidStack;
import martian.minefactorial.foundation.item.ChancedItemStack;
import martian.minefactorial.foundation.recipe.EntityRecipeInput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import static martian.minefactorial.Minefactorial.id;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeRanching(
		EntityIngredient ingredient,
		Optional<ChancedItemStack> outputItem,
		Optional<ChancedFluidStack> outputFluid
) implements Recipe<EntityRecipeInput> {
	public static final RecipeType<RecipeRanching> TYPE = RecipeType.simple(id("ranching"));
	public static final Serializer SERIALIZER = new Serializer();

	public ItemStack rollItemStack() {
		if (outputItem.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			return outputItem.get().roll();
		}
	}

	public FluidStack rollFluidStack() {
		if (outputFluid.isEmpty()) {
			return FluidStack.EMPTY;
		} else {
			return outputFluid.get().roll();
		}
	}

	@Override
	public boolean matches(EntityRecipeInput input, Level level) {
		return ingredient.test(input.entity());
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}

	@Override
	@ApiStatus.Obsolete
	public ItemStack assemble(EntityRecipeInput input, HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	@ApiStatus.Obsolete
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	@ApiStatus.Obsolete
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	public static class Serializer implements RecipeSerializer<RecipeRanching> {
		public static final MapCodec<RecipeRanching> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
				EntityIngredient.CODEC.fieldOf("ingredient").forGetter(RecipeRanching::ingredient),
				ChancedItemStack.CODEC.optionalFieldOf("outputItem").forGetter(RecipeRanching::outputItem),
				ChancedFluidStack.CODEC.optionalFieldOf("outputFluid").forGetter(RecipeRanching::outputFluid)
		).apply(it, RecipeRanching::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeRanching> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<RecipeRanching> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeRanching> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
