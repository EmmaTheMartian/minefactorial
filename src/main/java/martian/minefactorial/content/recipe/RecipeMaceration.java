package martian.minefactorial.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.minefactorial.foundation.item.ChancedItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;

import static martian.minefactorial.Minefactorial.id;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeMaceration(
		int powerPerTick,
		int craftDuration,
		Ingredient input,
		NonNullList<ChancedItemStack> results
) implements Recipe<SingleRecipeInput> {
	public static final RecipeType<RecipeMaceration> TYPE = RecipeType.simple(id("maceration"));
	public static final Serializer SERIALIZER = new Serializer();

	@Override
	public boolean matches(SingleRecipeInput recipeInput, Level level) {
		return input.test(recipeInput.item());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}

	@ApiStatus.Obsolete
	@Override
	public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@ApiStatus.Obsolete
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	public static class Serializer implements RecipeSerializer<RecipeMaceration> {
		public static final MapCodec<RecipeMaceration> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
				Codec.INT.fieldOf("powerPerTick").forGetter(RecipeMaceration::powerPerTick),
				Codec.INT.fieldOf("craftDuration").forGetter(RecipeMaceration::craftDuration),
				Ingredient.CODEC.fieldOf("input").forGetter(RecipeMaceration::input),
				NonNullList.codecOf(ChancedItemStack.CODEC).fieldOf("results").forGetter(RecipeMaceration::results)
		).apply(it, RecipeMaceration::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMaceration> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<RecipeMaceration> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeMaceration> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
