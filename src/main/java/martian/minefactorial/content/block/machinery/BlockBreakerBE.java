package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.FakePlayerHelpers;
import martian.minefactorial.foundation.block.AbstractSlottedMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockBreakerBE extends AbstractSlottedMachineBE {
	public static final int SLOTS = 5;

	public BlockBreakerBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.BREAKER.get(), SLOTS, pos, blockState);
	}

	protected Direction getFacing() {
		return getBlockState().getValue(BlockBreaker.FACING);
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return getFacing().getOpposite();
	}

	@Override
	public boolean checkForWork() {
		assert level != null;
		BlockPos pos = worldPosition.relative(getFacing());
		BlockState state = level.getBlockState(pos);
		return level.isInWorldBounds(pos) &&
				!state.isAir() &&
				!(state.getDestroySpeed(level, pos) < 0);
	}

	@Override
	public void doWork() {
		assert level != null;
		BlockPos pos = worldPosition.relative(getFacing());
		BlockState state = level.getBlockState(pos);
		// Iterate over the block's drops and add the drops to the breaker's inventory. If the item does not fit, we
		// instead will drop it in the world.
		FakePlayerHelpers.breakBlockAndGetDrops((ServerLevel) level, pos).forEach(stack -> {
			ItemStack remainder = giveItem(stack);
			if (!remainder.isEmpty()) {
				Vec3 centre = pos.getCenter();
				level.addFreshEntity(new ItemEntity(level, centre.x, centre.y, centre.z, remainder));
			}
		});
	}

	@Override
	public int getIdleTime() {
		return 50;
	}
}
