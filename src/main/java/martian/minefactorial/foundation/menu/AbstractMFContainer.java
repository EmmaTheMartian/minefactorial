package martian.minefactorial.foundation.menu;

import martian.minefactorial.content.block.BlockCapacitorBE;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// https://www.mcjty.eu/docs/1.20.4_neo/ep3#container
public abstract class AbstractMFContainer extends AbstractContainerMenu {
	protected final int slotCount;

	protected AbstractMFContainer(@Nullable MenuType<?> menuType, int slotCount, int containerId) {
		super(menuType, containerId);
		this.slotCount = slotCount;
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < this.slotCount) {
				if (!this.moveItemStackTo(itemStack2, this.slotCount, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, this.slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return itemStack;
	}

	protected int addSlotRange(Container container, int index, int x, int y, int count, int dx) {
		for (int i = 0; i < count; i++) {
			addSlot(new Slot(container, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	protected int addSlotBox(Container container, int index, int x, int y, int horizontalCount, int verticalCount, int dx, int dy) {
		for (int i = 0; i < verticalCount; i++) {
			index = addSlotRange(container, index, x, y, horizontalCount, dx);
			y += dy;
		}
		return index;
	}

	protected void addPlayerInventorySlots(Container playerInventory, int leftColumn, int topRow) {
		// Player inventory
		addSlotBox(playerInventory, 9, leftColumn, topRow, 9, 3, 18, 18);
		// Hotbar
		addSlotRange(playerInventory, 0, leftColumn, topRow + 58, 9, 18);
	}
}
