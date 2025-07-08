package martian.minefactorial.content.block.logistics;

import martian.minefactorial.api.block.AbstractGenericInventoryBE;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.block.ITickableBE;
import top.girlkisser.lazuli.api.inventory.GhostStackHandler;
import top.girlkisser.lazuli.api.world.AABBHelpers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockItemRouterBE extends AbstractGenericInventoryBE<GhostStackHandler> implements ITickableBE {
	public static final int SLOTS = 54;

	public final int cooldownTicks = 8;
	public int cooldownTicksLeft = cooldownTicks;

	public BlockItemRouterBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.ITEM_ROUTER.get(), SLOTS, pos, blockState);
	}

	protected Direction determineDirectionForSlot(int slot) {
		if (slot < 9) {
			return Direction.UP;
		} else if (slot < 9 * 2) {
			return Direction.DOWN;
		} else if (slot < 9 * 3) {
			return Direction.NORTH;
		} else if (slot < 9 * 4) {
			return Direction.SOUTH;
		} else if (slot < 9 * 5) {
			return Direction.EAST;
		} else if (slot < 9 * 6) {
			return Direction.WEST;
		} else {
			return null;
		}
	}

	protected Direction determineDirectionForItem(ItemStack item) {
		for (int i = 0; i < SLOTS; i++) {
			if (ItemStack.isSameItemSameComponents(getItem(i), item)) {
				return determineDirectionForSlot(i);
			}
		}
		return null;
	}

	@Override
	protected GhostStackHandler makeItemStackHandler() {
		return new GhostStackHandler(SLOTS) {
			@Override
			public void onContentsChanged(int slot) {
				BlockItemRouterBE.this.setChanged();
			}
		};
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (--cooldownTicksLeft <= 0) {
			List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, AABBHelpers.ofBlock(worldPosition).inflate(0.1f));
			if (!entities.isEmpty()) {
				cooldownTicksLeft = cooldownTicks;
				for (ItemEntity itemEntity : entities) {
					ItemStack stack = itemEntity.getItem();
					Direction direction = determineDirectionForItem(stack);
					if (direction == null) {
						continue;
					}
					itemEntity.setDeltaMovement(0, 0, 0);
					itemEntity.setPos(worldPosition.getCenter().relative(direction, 1.2f));
				}
			}
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("CooldownTicksLeft")) {
			cooldownTicksLeft = tag.getInt("CooldownTicksLeft");
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		tag.putInt("CooldownTicksLeft", cooldownTicksLeft);
	}
}
