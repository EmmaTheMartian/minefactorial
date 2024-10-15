package martian.minefactorial.foundation.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class MultiTank implements IFluidTank {
	@Override
	public FluidStack getFluid() {
		return null;
	}

	@Override
	public int getFluidAmount() {
		return 0;
	}

	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public boolean isFluidValid(FluidStack fluidStack) {
		return false;
	}

	@Override
	public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
		return 0;
	}

	@Override
	public FluidStack drain(int i, IFluidHandler.FluidAction fluidAction) {
		return null;
	}

	@Override
	public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
		return null;
	}
}
