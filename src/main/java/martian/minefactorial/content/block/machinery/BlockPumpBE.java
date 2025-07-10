package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.api.block.AbstractSingleTankMachineBE;
import martian.minefactorial.api.fluid.MFFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import top.girlkisser.lazuli.api.block.IFluidBE;

public class BlockPumpBE extends AbstractSingleTankMachineBE {
	public BlockPumpBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PUMP.get(), 4000, pos, blockState);
	}

	@Override
	protected MFFluidTank makeFluidTank() {
		MFFluidTank tank = new MFFluidTank(this.capacity, this::validate) {
			@Override
			public void onContentsChanged() {
				BlockPumpBE.this.setChanged();
			}
		};
		tank.canReceive = false;
		return tank;
	}

	protected BlockPos getTargetPos() {
		return getBlockPos().relative(getBlockState().getValue(BlockPump.FACING));
	}

	protected FluidState getTargetFluidState(ServerLevel level) {
		return level.getFluidState(getTargetPos());
	}

	@Override
	public int getIdleTime() {
		return 40;
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (getTank().getFluidAmount() > 0) {
			Direction direction = getBlockState().getValue(BlockPump.FACING).getOpposite();
			IFluidBE.tryPushFluid(getTank(), level, getMaxFluidExtract(), this, direction);
		}

		super.serverTick(level);
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		return getTank().getSpace() >= 1000 && // Make sure we have at least 1000mB of fluid storage available
				!getTargetFluidState(level).is(Fluids.EMPTY); // Make sure the fluid in front of the pump actually exists
	}

	@Override
	public void doWork(ServerLevel level) {
		((MFFluidTank) getTank()).forceFill(new FluidStack(getTargetFluidState(level).getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
		level.setBlockAndUpdate(getTargetPos(), Blocks.AIR.defaultBlockState());
	}
}
