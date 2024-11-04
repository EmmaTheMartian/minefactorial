package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockPumpBE extends AbstractSingleTankMachineBE {
	public BlockPumpBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PUMP.get(), 4000, pos, blockState);
	}

	protected BlockPos getTargetPos() {
		return getBlockPos().relative(getBlockState().getValue(BlockPump.FACING));
	}

	protected FluidState getTargetFluidState() {
		assert level != null;
		return level.getFluidState(getTargetPos());
	}

	@Override
	public int getIdleTime() {
		return 40;
	}

	@Override
	public void serverTick() {
		if (getTank().getFluidAmount() > 0) {
			assert level != null;
			Direction direction = getBlockState().getValue(BlockPump.FACING).getOpposite();
			IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(direction), direction);
			if (fluidHandler != null) {
				int amount = Math.min(getTank().getFluidAmount(), getMaxFluidExtract());
				FluidStack stack = getTank().getFluid().copyWithAmount(amount);
				fluidHandler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
				getTank().drain(amount, IFluidHandler.FluidAction.EXECUTE);
			}
		}

		super.serverTick();
	}

	@Override
	public boolean checkForWork() {
		return getTank().getSpace() >= 1000 && // Make sure we have at least 1000mB of fluid storage available
				!getTargetFluidState().is(Fluids.EMPTY); // Make sure the fluid in front of the pump actually exists
	}

	@Override
	public void doWork() {
		assert level != null;
		getTank().fill(new FluidStack(getTargetFluidState().getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
		level.setBlockAndUpdate(getTargetPos(), Blocks.AIR.defaultBlockState());
	}
}
