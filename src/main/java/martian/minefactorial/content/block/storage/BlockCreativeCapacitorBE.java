package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.girlkisser.lazuli.api.block.AbstractCapacitorBE;

import javax.annotation.Nullable;

public class BlockCreativeCapacitorBE extends AbstractCapacitorBE {
	public BlockCreativeCapacitorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CREATIVE_CAPACITOR.get(), pos, blockState);
	}

	@Override
	public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
		return getEnergyStorage();
	}

	@Override
	public int getMaxEnergyReceive() {
		return 0;
	}

	@Override
	public int getMaxEnergy() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void serverTick(ServerLevel level) {
		this.getEnergyStorage().forceReceiveEnergy(Integer.MAX_VALUE, false);
		super.serverTick(level);
	}
}
