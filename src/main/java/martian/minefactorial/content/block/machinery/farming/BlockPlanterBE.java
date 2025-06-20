package martian.minefactorial.content.block.machinery.farming;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.FakePlayerHelpers;
import martian.minefactorial.foundation.block.AbstractZonedInventoryMachineBE;
import martian.minefactorial.foundation.item.RoundRobinInventory;
import martian.minefactorial.foundation.world.AABBHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockPlanterBE extends AbstractZonedInventoryMachineBE {
	public static final int SLOTS = 9;

	public BlockPlanterBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PLANTER.get(), SLOTS, pos, blockState);
	}

	public boolean hasNoSeeds() {
		for (int i = 0 ; i < this.getContainerSize() ; i++) {
			if (this.getItem(i).getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CropBlock) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected ItemStackHandler makeItemStackHandler() {
		return new RoundRobinInventory(this.slots) {
			@Override
			public void onContentsChanged(int slot) {
				BlockPlanterBE.this.setChanged();
			}
		};
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
		return Direction.DOWN;
	}

	@Override
	public AABB getWorkZone() {
		BlockPos origin = getBlockPos().relative(Direction.UP, 2);
		BlockPos start = origin
				.relative(Direction.NORTH, getWorkZoneRange())
				.relative(Direction.WEST, getWorkZoneRange());
		BlockPos end = origin
				.relative(Direction.SOUTH, getWorkZoneRange())
				.relative(Direction.EAST, getWorkZoneRange());
		return AABB.encapsulatingFullBlocks(start, end);
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		if (hasNoSeeds()) {
			return false;
		}

		// Check for dirt or grass to till
		AtomicBoolean result = new AtomicBoolean(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get().move(new BlockPos(0, -1, 0))).forEach(pos -> {
			if (result.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
				result.set(true);
			}
		});
		if (result.get()) {
			return true;
		}

		// Check for soil without crops
		result.set(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
			if (result.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (state.isAir() && level.getBlockState(pos.below()).is(Blocks.FARMLAND)) {
				result.set(true);
			}
		});
		return result.get();
	}

	@Override
	public void doWork(ServerLevel level) {
		// Till dirt
		AtomicBoolean done = new AtomicBoolean(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get().move(new BlockPos(0, -1, 0))).forEach(pos -> {
			if (done.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
				BlockState tilledState = getTilled(level, state, pos);
				if (tilledState == null || tilledState == state) {
					return;
				}
				level.setBlockAndUpdate(pos, tilledState);
				done.set(true);
			}
		});

		// Plant crops
		if (hasNoSeeds()) {
			return;
		}

		done.set(false);
		AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
			if (done.get()) {
				return;
			}
			BlockState state = level.getBlockState(pos);
			if (state.isAir() && level.getBlockState(pos.below()).is(Blocks.FARMLAND)) {
				int slot = getSlotForGivenPos(pos);
				ItemStack crop = getItem(slot);
				if (crop.isEmpty()) {
					return;
				}
				if (crop.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CropBlock cropBlock) {
					crop.shrink(1);
					level.setBlockAndUpdate(pos, cropBlock.defaultBlockState());
				}
				done.set(true);
			}
		});
	}

	public int getSlotForGivenPos(BlockPos pos) {
		BlockPos givenPos = pos.subtract(this.worldPosition);

		if (givenPos.getX() > 0 && givenPos.getZ() > 0) {
			return 0;
		} else if (givenPos.getX() == 0 && givenPos.getZ() > 0) {
			return 1;
		} else if (givenPos.getX() < 0 && givenPos.getZ() > 0) {
			return 2;
		} else if (givenPos.getX() > 0 && givenPos.getZ() == 0) {
			return 3;
		} else if (givenPos.getX() == 0 && givenPos.getZ() == 0) {
			return 4;
		} else if (givenPos.getX() < 0 && givenPos.getZ() == 0) {
			return 5;
		} else if (givenPos.getX() > 0 && givenPos.getZ() < 0) {
			return 6;
		} else if (givenPos.getX() == 0 && givenPos.getZ() < 0) {
			return 7;
		} else if (givenPos.getX() < 0 && givenPos.getZ() < 0) {
			return 8;
		} else {
			return 4;
		}
	}

	public ItemStack getHoe() {
		return new ItemStack(Items.DIAMOND_HOE);
	}

	public BlockState getTilled(ServerLevel level, BlockState state, BlockPos pos) {
		return state.getToolModifiedState(FakePlayerHelpers.getUseOnContext(level, getHoe(), pos), ItemAbilities.HOE_TILL, true);
	}
}
