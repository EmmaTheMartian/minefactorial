package martian.minefactorial.foundation.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MFItemStackHandler extends ItemStackHandler implements ISerializableItemHandler {
	public MFItemStackHandler() {
		this(1);
	}

	public MFItemStackHandler(int size) {
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public MFItemStackHandler(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	/** Returns the remainder, if there was one. */
	public static ItemStack insertItem(ItemStackHandler handler, ItemStack stack, boolean simulate) {
		ItemStack remainder = stack.copy();
		for (int i = 0; i < handler.getSlots(); i++) {
			if (handler.insertItem(i, stack, true) != stack) {
				remainder = handler.insertItem(i, stack, simulate);
				if (remainder == ItemStack.EMPTY) {
					break;
				}
			}
		}
		return remainder;
	}
}
