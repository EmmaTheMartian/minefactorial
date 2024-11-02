package martian.minefactorial.foundation.block;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface ISingleTankBE {
	/** Tank capacity for the block */
	default int getTankCapacity() {
		return getTank().getTankCapacity(0);
	}

	/** Maximum fluid extractable per tick for the block */
	default int getMaxFluidExtract() {
		return 32;
	}

	/** Maximum fluid receivable per tick for the block */
	default int getMaxFluidReceive() {
		return 32;
	}

	FluidTank getTank();
}
