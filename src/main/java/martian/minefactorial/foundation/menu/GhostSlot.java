package martian.minefactorial.foundation.menu;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GhostSlot extends Slot {
	public GhostSlot(Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}

	public ItemStack safeInsert(ItemStack stack, int increment) {
		ItemStack held = stack.copy();
		super.safeInsert(stack, increment);
		return held;
	}

	public boolean isFake() {
		return true;
	}
}
