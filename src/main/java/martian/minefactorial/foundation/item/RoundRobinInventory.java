package martian.minefactorial.foundation.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class RoundRobinInventory extends ItemStackHandler {
	protected int index = 0;

	public RoundRobinInventory() {
		super();
	}

	public RoundRobinInventory(int size) {
		super(size);
	}

	public RoundRobinInventory(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	@Override
	@ApiStatus.Internal
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		// We will be disregarding `slot` here because we want to round-robin
		// distribute items
		ItemStack result = super.insertItem(index++, stack, simulate);
		if (index >= this.getSlots()) {
			index = 0;
		}
		return result;
	}

	public ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
		return insertItem(0, stack, simulate);
	}
}
