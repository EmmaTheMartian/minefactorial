package martian.minefactorial.foundation.menu;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SlotOutputOnly extends Slot {
	public SlotOutputOnly(Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}

	public ItemStack safeInsert(ItemStack stack) {
		return stack;
	}

	public ItemStack safeInsert(ItemStack stack, int increment) {
		return this.safeInsert(stack);
	}

	public boolean allowModification(Player player) {
		return false;
	}

	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}
