package martian.minefactorial.content.block.power;

import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import top.girlkisser.lazuli.api.block.AbstractGeneratorBE;
import top.girlkisser.lazuli.api.block.ISingleTankBE;

public class BlockSteamTurbineBE extends AbstractGeneratorBE implements ISingleTankBE {
	public final int STEAM_PER_WORK = 2, ENERGY_GENERATION_PER_WORK = 1;

	private final FluidTank tank = new FluidTank(2000, fluidStack -> fluidStack.is(MFTags.Fluids.STEAM)) {
		@Override
		protected void onContentsChanged() {
			setChanged();
		}
	};

	public BlockSteamTurbineBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.STEAM_TURBINE.get(), pos, blockState);
	}

	@Override
	public FluidTank getTank() {
		return tank;
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (tank.getFluidAmount() > STEAM_PER_WORK && this.getMaxEnergy() > this.getEnergyStored()) {
			tank.drain(STEAM_PER_WORK, IFluidHandler.FluidAction.EXECUTE);
			this.getEnergyStorage().forceReceiveEnergy(ENERGY_GENERATION_PER_WORK, false);
			this.setChanged();
		}

		if (this.getEnergyStored() > 0) {
			this.distributePower();
		}
	}
}
