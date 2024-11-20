package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.recipe.RecipeMaceration;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFRecipeTypes;
import martian.minefactorial.foundation.ArrayHelpers;
import martian.minefactorial.foundation.block.AbstractSlottedMachineBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockMaceratorBE extends AbstractSlottedMachineBE {
	public static final int INPUT_SLOTS = 1, OUTPUT_SLOTS = 4;
	public static final int[]
			INPUT_SLOT_INDEXES = ArrayHelpers.rangeOf(INPUT_SLOTS),
			OUTPUT_SLOT_INDEXES = ArrayHelpers.rangeOf(INPUT_SLOTS, OUTPUT_SLOTS);

	protected ItemStack consumedItem = ItemStack.EMPTY;
	protected @Nullable RecipeHolder<RecipeMaceration> recipe = null;

	public BlockMaceratorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.MACERATOR.get(), INPUT_SLOTS + OUTPUT_SLOTS, pos, blockState);
	}

	public boolean checkForRecipe(ServerLevel level) {
		ItemStack toMacerate = getItem(0).copyWithCount(1);
		SingleRecipeInput input = new SingleRecipeInput(toMacerate);

		level.getRecipeManager()
				.getRecipeFor(MFRecipeTypes.MACERATION.get(), input, level)
				.ifPresent(recipe -> {
					// Check if we actually have energy to start the craft
					if (getEnergyStored() < recipe.value().powerPerTick()) {
						return;
					}
					consumedItem = toMacerate;
					getItem(0).shrink(1);
					this.recipe = recipe;
				});

		if (this.recipe != null) {
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMacerator.RUNNING, true));
		}

		return this.recipe == null;
	}

	@Override
	public int getMaxWork() {
		if (recipe != null) {
			return recipe.value().craftDuration();
		}
		return 60;
	}

	@Override
	public int getIdleTime() {
		return 40;
	}

	@Override
	public int getEnergyPerWork() {
		return 0;
	}

	@Override
	public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
		return side == Direction.DOWN ? OUTPUT_SLOT_INDEXES : INPUT_SLOT_INDEXES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, Direction direction) {
		return ArrayHelpers.contains(getSlotsForFace(direction), index);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
		return ArrayHelpers.contains(getSlotsForFace(direction), index);
	}

	@Override
	public boolean canEjectSlot(int slot) {
		return slot != 0; // 0 is the input slot
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockMacerator.FACING).getOpposite();
	}

	@Override
	public boolean afterIdle(ServerLevel level) {
		return checkForRecipe(level);
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		return true;
	}

	@Override
	public boolean onWorkTick(ServerLevel level) {
		if (recipe != null) {
			getEnergyStorage().forceExtractEnergy(recipe.value().powerPerTick(), false);
			if (getEnergyStored() <= 0) {
				recipe = null;
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMacerator.RUNNING, false));
				giveItem(consumedItem.copyAndClear()); // Refund lost item
				return true;
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doWork(ServerLevel level) {
		if (recipe != null) {
			recipe.value().results().forEach(stack -> {
				ItemStack rolled = stack.roll();
				if (!rolled.isEmpty()) {
					IInventoryBE.insertItemInto(this.inventory, rolled, INPUT_SLOTS, OUTPUT_SLOTS);
				}
			});
			recipe = null;
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMacerator.RUNNING, false));
			// Immediately check if there is another recipe available to craft. If so then we don't want to go idle
			checkForRecipe(level);
		}
	}
}
