package martian.minefactorial.foundation.fluid;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record FluidStackInput(FluidStack fluid) implements RecipeInput {
	@Override
	public @NotNull ItemStack getItem(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return fluid.isEmpty();
	}
}
