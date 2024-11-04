package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankBE;
import martian.minefactorial.foundation.block.ISingleTankBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockCreativeTankBE extends AbstractSingleTankBE implements ISingleTankBE, ITickableBE {
	public BlockCreativeTankBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CREATIVE_TANK.get(), Integer.MAX_VALUE, pos, blockState);
	}

	@Override
	public int getMaxFluidReceive() {
		return 0;
	}

	@Override
	public void serverTick() {
		getTank().fill(new FluidStack(getTank().getFluid().getFluid(), Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
		setChanged();
	}
}
