package martian.minefactorial.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.ApiStatus;
import top.girlkisser.lazuli.api.crafting.FluidStackInput;

import javax.annotation.ParametersAreNonnullByDefault;

import static martian.minefactorial.Minefactorial.id;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RecipeMeatPacking(
		int powerPerTick,
		int craftDuration,
		FluidIngredient input,
		int inputAmount,
		ItemStack result
) implements Recipe<FluidStackInput> {
	public static final RecipeType<RecipeMeatPacking> TYPE = RecipeType.simple(id("meat_packing"));
	public static final Serializer SERIALIZER = new Serializer();

	@Override
	public boolean matches(FluidStackInput recipeInput, Level level) {
		return input.test(recipeInput.fluid()) && recipeInput.fluid().getAmount() >= inputAmount;
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
	public ItemStack assemble(FluidStackInput input, HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@ApiStatus.Obsolete
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	public static class Serializer implements RecipeSerializer<RecipeMeatPacking> {
		public static final MapCodec<RecipeMeatPacking> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
				Codec.INT.fieldOf("powerPerTick").forGetter(RecipeMeatPacking::powerPerTick),
				Codec.INT.fieldOf("craftDuration").forGetter(RecipeMeatPacking::craftDuration),
				FluidIngredient.CODEC.fieldOf("input").forGetter(RecipeMeatPacking::input),
				Codec.INT.fieldOf("inputAmount").forGetter(RecipeMeatPacking::inputAmount),
				ItemStack.CODEC.fieldOf("result").forGetter(RecipeMeatPacking::result)
		).apply(it, RecipeMeatPacking::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMeatPacking> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

		@Override
		public MapCodec<RecipeMeatPacking> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RecipeMeatPacking> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
