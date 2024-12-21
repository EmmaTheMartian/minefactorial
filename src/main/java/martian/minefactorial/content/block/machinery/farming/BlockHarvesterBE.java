package martian.minefactorial.content.block.machinery.farming;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.FakePlayerHelpers;
import martian.minefactorial.foundation.block.AbstractZonedInventoryMachineBE;
import martian.minefactorial.foundation.world.AABBHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockHarvesterBE extends AbstractZonedInventoryMachineBE {
	public static final int SLOTS = 5;

	public BlockHarvesterBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.HARVESTER.get(), SLOTS, pos, blockState);
	}

	@Override
	public int getIdleTime() {
		return 100;
	}

	@Override
	public int getMaxWork() {
		return 10;
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockHarvester.FACING).getOpposite();
	}

	@Override
	public void onWorkStart(ServerLevel level) {
		level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(BlockHarvester.RUNNING, true));
	}

	@Override
	public void onWorkStop(ServerLevel level) {
		level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(BlockHarvester.RUNNING, false));
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		if (this.isInventoryFull()) {
			return false;
		}

		AtomicBoolean result = new AtomicBoolean(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
			if (result.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (!state.isAir() && state.getBlock() instanceof CropBlock cropBlock) {
				if (cropBlock.isMaxAge(state)) {
					result.set(true);
				}
			}
		});
		return result.get();
	}

	@Override
	public void doWork(ServerLevel level) {
		AtomicBoolean done = new AtomicBoolean(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
			if (done.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (!state.isAir() && state.getBlock() instanceof CropBlock cropBlock) {
				if (cropBlock.isMaxAge(state)) {
					FakePlayerHelpers.breakBlockAndGetDrops(level, pos).forEach(stack -> {
						ItemStack remainder = giveItem(stack);
						if (!remainder.isEmpty()) {
							Vec3 centre = pos.getCenter();
							level.addFreshEntity(new ItemEntity(level, centre.x, centre.y, centre.z, remainder));
						}
					});
					done.set(true);
				}
			}
		});
	}
}
