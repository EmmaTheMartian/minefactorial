package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.recipe.RecipeMeatPacking;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFRecipeTypes;
import martian.minefactorial.api.block.AbstractSingleTankAndInventoryMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.block.IInventoryBE;
import top.girlkisser.lazuli.api.crafting.FluidStackInput;

import javax.annotation.Nullable;

public class BlockMeatPackerBE extends AbstractSingleTankAndInventoryMachineBE {
	public static final int TANK_CAPACITY = 4000;
	public static final int SLOTS = 1;

	protected FluidStack consumedFluid = FluidStack.EMPTY;
	protected @Nullable RecipeHolder<RecipeMeatPacking> recipe = null;

	public BlockMeatPackerBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.MEAT_PACKER.get(), TANK_CAPACITY, SLOTS, pos, blockState);
	}

	public boolean checkForRecipe(ServerLevel level) {
		if (getTank().getFluidAmount() < 1000) {
			return false;
		}

		FluidStack toPack = getTank().getFluid();
		FluidStackInput input = new FluidStackInput(toPack);

		level.getRecipeManager()
				.getRecipeFor(MFRecipeTypes.MEAT_PACKING.get(), input, level)
				.ifPresent(recipe -> {
					// Check if we actually have energy to start the craft
					if (getEnergyStored() < recipe.value().powerPerTick()) {
						return;
					}
					consumedFluid = toPack;
					getTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
					this.recipe = recipe;
				});

		if (this.recipe != null) {
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMeatPacker.RUNNING, true));
		}

		return this.recipe == null;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, Direction direction) {
		return false;
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
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockMeatPacker.FACING).getOpposite();
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
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMeatPacker.RUNNING, false));
				getTank().fill(consumedFluid.copyAndClear(), IFluidHandler.FluidAction.EXECUTE); // Refund lost fluid if able
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
			IInventoryBE.insertItemInto(this.inventory, recipe.value().result().copy());
			recipe = null;
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockMeatPacker.RUNNING, false));
			// Immediately check if there is another recipe available to craft. If so then we don't want to go idle
			checkForRecipe(level);
		}
	}
}
