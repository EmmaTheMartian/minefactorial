package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public interface IInventoryBE extends IBE, Container {
	ItemStackHandler getInventory();

	/**
	 * @return If the inventory is full. This does NOT account for stack sizes.
	 */
	default boolean isInventoryFull() {
		for (int i = 0; i < getInventory().getSlots(); i++) {
			if (getInventory().getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	default int getContainerSize() {
		return getInventory().getSlots();
	}

	default boolean isEmpty() {
		for (int i = 0; i < getInventory().getSlots(); i++) {
			if (!getInventory().getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	default @NotNull ItemStack getItem(int slot) {
		return getInventory().getStackInSlot(slot);
	}

	default @NotNull ItemStack removeItem(int slot, int amount) {
		return getInventory().extractItem(slot, amount, false);
	}

	default @NotNull ItemStack removeItemNoUpdate(int slot) {
		ItemStack stack = getInventory().getStackInSlot(slot).copy();
		getInventory().setStackInSlot(slot, ItemStack.EMPTY);
		return stack;
	}

	default void setItem(int slot, @NotNull ItemStack stack) {
		getInventory().setStackInSlot(slot, stack);
	}

	default void setChanged() { }

	default boolean stillValid(@NotNull Player player) {
		return ContainerLevelAccess.create(getLevel(), getBlockPos()).evaluate((level, pos) ->
				player.canInteractWithBlock(pos, player.blockInteractionRange()), true);
	}

	default void clearContent() {
		for (int i = 0; i < getInventory().getSlots(); i++) {
			getInventory().setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	default int getFirstStackIndex() {
		for (int i = 0; i < getInventory().getSlots(); i++) {
			if (!getItem(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	default ItemStack giveItem(final ItemStack stack) {
		return insertItemInto(getInventory(), stack);
	}

	default boolean shouldEjectItems() {
		return true;
	}

	default Direction getEjectDirection(BlockState state) {
		if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
			return state.getValue(HorizontalDirectionalBlock.FACING);
		} else if (state.hasProperty(DirectionalBlock.FACING)) {
			return state.getValue(DirectionalBlock.FACING);
		} else {
			return Direction.NORTH;
		}
	}

	default int getRandomUsedSlot(RandomSource random) {
		int slot = -1;
		int j = 1;

		for (int i = 0; i < getInventory().getSlots(); i++) {
			if (!getInventory().getStackInSlot(i).isEmpty() && random.nextInt(j++) == 0) {
				slot = i;
			}
		}

		return slot;
	}

	static void ejectFrom(ServerLevel level, BlockPos pos, Direction direction, int slot, int maxCount) {
		if (!(level.getBlockEntity(pos) instanceof IInventoryBE be)) {
			return;
		}

		int i = slot == -1 ? be.getRandomUsedSlot(level.random) : slot;
		if (i < 0 || i > be.getInventory().getSlots()) {
			return;
		}

		ItemStack stack = be.getItem(i).copy();
		if (!stack.isEmpty()) {
			// Adapted from net.minecraft.core.dispenser.DefaultDispenseItemBehavior
			BlockPos ejectBlockPos = pos.relative(direction);
			Position ejectPos = ejectBlockPos.getCenter();
			ItemStack toEject = stack.split(Math.min(stack.getCount(), maxCount));

			if (toEject.isEmpty()) {
				return;
			}

			// If the block in the eject direction has an item handler, we can try to dump the item into it instantly
			IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, ejectBlockPos, direction);
			if (itemHandler != null) {
				ItemStack remainder = insertItemInto(itemHandler, toEject);
				if (!remainder.isEmpty()) {
					// There was a remainder left, meaning that the item handler is probably full.
					return;
				}
				level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
				level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());
				be.setItem(i, remainder);
				return;
			}

			// If the block in the eject direction did not have an item handler and is a full block, we cannot eject
			if (level.getBlockState(ejectBlockPos).isCollisionShapeFullBlock(level, ejectBlockPos)) {
				return;
			}

			// No obstructions, so we can eject the item
			double yPos = ejectPos.y();
			if (direction.getAxis() == Direction.Axis.Y) {
				yPos -= 0.125;
			} else {
				yPos -= 0.15625;
			}

			ItemEntity ie = new ItemEntity(level, ejectPos.x(), yPos, ejectPos.z(), toEject);
			ie.setDeltaMovement(0, 0, 0);
			level.addFreshEntity(ie);

			level.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pos, 0);
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, direction.get3DDataValue());
			be.setItem(i, stack);
		}
	}

	static void ejectFrom(IInventoryBE be, int maxAmount) {
		ejectFrom((ServerLevel) be.getLevel(), be.getBlockPos(), be.getEjectDirection(be.getBlockState()), -1, maxAmount);
	}

	static ItemStack insertItemInto(IItemHandler itemHandler, ItemStack stack) {
		ItemStack copy = stack.copy();
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			// Insert the item and yoink the remainder
			ItemStack remainder = itemHandler.insertItem(i, copy, false);
			// If the remainder is empty or its count is different from the stack's, then something changed
			if (remainder.isEmpty() || remainder.getCount() != copy.getCount()) {
				if (remainder.isEmpty()) {
					return ItemStack.EMPTY;
				}
				copy = remainder;
			}
		}
		return copy;
	}
}
