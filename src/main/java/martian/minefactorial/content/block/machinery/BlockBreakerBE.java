package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBreakerBE extends AbstractMachineBE {
	public BlockBreakerBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.BREAKER.get(), pos, blockState);
	}

	protected Direction getFacing() {
		return getBlockState().getValue(BlockBreaker.FACING);
	}

	@Override
	public boolean checkForWork() {
		assert level != null;
		BlockPos pos = worldPosition.relative(getFacing());
		BlockState state = level.getBlockState(pos);
		return level.isInWorldBounds(pos) && !state.isAir() && !(state.getDestroySpeed(level, pos) < 0);
	}

	@Override
	public void doWork() {
		assert level != null;
		level.destroyBlock(worldPosition.relative(getFacing()), true);
	}

	@Override
	public int getIdleTime() {
		return 50;
	}
}
