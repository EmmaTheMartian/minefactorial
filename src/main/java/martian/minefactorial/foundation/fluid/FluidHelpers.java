package martian.minefactorial.foundation.fluid;

import martian.minefactorial.foundation.block.ISingleTankBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class FluidHelpers {
	private FluidHelpers() { }

	public static <T extends BlockEntity & ISingleTankBE> void tryDistributeFluid(Level level, T be) {
		tryDistributeFluid(be.getTank(), level, be.getMaxFluidExtract(), be);
	}

	public static <T extends BlockEntity> void tryDistributeFluid(IFluidTank tank, Level level, int maxExtract, T be) {
		if (tank.getFluidAmount() <= 0) {
			return;
		}

		for (Direction direction : Direction.values()) {
			if (tank.getFluidAmount() <= 0) {
				return;
			}

			FluidStack stack = tank.getFluid();
			IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos().relative(direction), direction);
			if (storage != null) {
				int toPush = Math.min(stack.getAmount(), maxExtract);
				int received = storage.fill(stack.copyWithAmount(toPush), IFluidHandler.FluidAction.EXECUTE);
				tank.drain(received, IFluidHandler.FluidAction.EXECUTE);
				be.setChanged();
				break;
			}
		}
	}

	public static <T extends BlockEntity> void tryPushFluid(IFluidTank tank, Level level, int maxExtract, T be, Direction direction) {
		if (tank.getFluidAmount() <= 0) {
			return;
		}

		FluidStack stack = tank.getFluid();
		IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos().relative(direction), direction);
		if (storage != null) {
			int toPush = Math.min(stack.getAmount(), maxExtract);
			int received = storage.fill(stack.copyWithAmount(toPush), IFluidHandler.FluidAction.EXECUTE);
			tank.drain(received, IFluidHandler.FluidAction.EXECUTE);
			be.setChanged();
		}
	}
}
