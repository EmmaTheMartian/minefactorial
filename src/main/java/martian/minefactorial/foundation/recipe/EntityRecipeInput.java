package martian.minefactorial.foundation.recipe;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public record EntityRecipeInput(EntityType<?> entity) implements RecipeInput {
	@Override
	@ApiStatus.Obsolete
	public @NotNull ItemStack getItem(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
