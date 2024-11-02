package martian.minefactorial.foundation.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MFItemStackHandler extends ItemStackHandler implements ISerializableItemHandler {
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
