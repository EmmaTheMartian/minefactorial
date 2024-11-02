package martian.minefactorial.foundation.fluid;

import martian.minefactorial.foundation.block.ISingleTankBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class FluidHelpers {
	private FluidHelpers() { }

	public static <T extends BlockEntity & ISingleTankBE> void tryDistributeFluid(Level level, T be) {
		if (be.getTank().getFluidAmount() <= 0) {
			return;
		}

		for (Direction direction : Direction.values()) {
			if (be.getTank().getFluidAmount() <= 0) {
				return;
			}

			FluidStack stack = be.getTank().getFluid();
			IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos().relative(direction), direction);
			if (storage != null) {
				int toPush = Math.min(stack.getAmount(), be.getMaxFluidExtract());
				int received = storage.fill(stack.copyWithAmount(toPush), IFluidHandler.FluidAction.EXECUTE);
				be.getTank().drain(received, IFluidHandler.FluidAction.EXECUTE);
				be.setChanged();
				break;
			}
		}
	}
}
