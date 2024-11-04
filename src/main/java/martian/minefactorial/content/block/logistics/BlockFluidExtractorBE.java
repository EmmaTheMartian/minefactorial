package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockFluidExtractorBE extends AbstractSingleTankBE implements ITickableBE {
	public BlockFluidExtractorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.FLUID_EXTRACTOR.get(), 1000, pos, blockState);
	}

	@Override
	public void serverTick() {
		assert level != null;

		Direction facing = getBlockState().getValue(BlockFluidExtractor.FACING);

		IFluidHandler extractStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(facing.getOpposite()), facing.getOpposite());
		if (extractStorage != null && getTank().getSpace() > 0) {
			FluidStack simulationDrained = extractStorage.drain(getMaxFluidReceive(), IFluidHandler.FluidAction.SIMULATE);
			if (simulationDrained.is(getTank().getFluid().getFluid()) || getTank().isEmpty()) {
				int filled = this.getTank().fill(simulationDrained, IFluidHandler.FluidAction.EXECUTE);
				extractStorage.drain(filled, IFluidHandler.FluidAction.EXECUTE);
			}
		}

		IFluidHandler pushStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(facing), facing);
		if (pushStorage != null && getTank().getFluidAmount() > 0) {
			FluidStack toPush = getTank().getFluid().copyWithAmount(Math.min(getMaxFluidExtract(), getTank().getFluidAmount()));
			int filled = pushStorage.fill(toPush, IFluidHandler.FluidAction.EXECUTE);
			getTank().drain(filled, IFluidHandler.FluidAction.EXECUTE);
		}
	}
}
