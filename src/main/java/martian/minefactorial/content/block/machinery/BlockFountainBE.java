package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockFountainBE extends AbstractSingleTankMachineBE {
	public BlockFountainBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.FOUNTAIN.get(), 4000, pos, blockState);
	}

	protected boolean fluidHasBlock() {
		return !getTank()
				.getFluid()
				.getFluid()
				.defaultFluidState()
				.createLegacyBlock()
				.isAir();
	}

	@Override
	public int getIdleTime() {
		return 40;
	}

	@Override
	public boolean checkForWork() {
		assert level != null;
		return getTank().getFluidAmount() >= 1000 &&
				level.getBlockState(worldPosition.above()).isAir() &&
				fluidHasBlock();
	}

	@Override
	public void doWork() {
		if (!fluidHasBlock()) {
			return;
		}

		FluidStack drained = getTank().drain(1000, IFluidHandler.FluidAction.SIMULATE);
		if (drained.getAmount() != 1000) {
			return;
		}

		assert level != null;
		level.setBlockAndUpdate(worldPosition.above(), drained.getFluid().defaultFluidState().createLegacyBlock());
		getTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
	}
}
