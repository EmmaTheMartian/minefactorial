package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractConveyorBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import martian.minefactorial.foundation.world.AABBHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockHoppingConveyorBE extends AbstractConveyorBE {
	public final int cooldownTicks = 8;
	public int cooldownTicksLeft = cooldownTicks;
	public int maxInsert = 64;

	public BlockHoppingConveyorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.HOPPING_CONVEYOR.get(), pos, blockState);
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (--cooldownTicksLeft <= 0) {
			IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, this.worldPosition.below(), Direction.UP);
			if (handler != null) {
				List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, AABBHelpers.ofBlock(worldPosition));
				if (!entities.isEmpty()) {
					cooldownTicksLeft = cooldownTicks;
					for (ItemEntity itemEntity : entities) {
						ItemStack stack = itemEntity.getItem();
						ItemStack toInsert = stack.copyWithCount(Math.min(stack.getCount(), maxInsert));
						ItemStack remainder = IInventoryBE.insertItemInto(handler, toInsert);
						if (remainder.isEmpty()) {
							itemEntity.setItem(stack.copyWithCount(stack.getCount() - toInsert.getCount()));
						} else {
							itemEntity.setItem(stack.copyWithCount(stack.getCount() - remainder.getCount()));
						}
					}
				}
			}
		}

		super.serverTick(level);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("MaxInsert")) {
			maxInsert = tag.getInt("MaxInsert");
		}

		if (tag.contains("CooldownTicksLeft")) {
			cooldownTicksLeft = tag.getInt("CooldownTicksLeft");
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		tag.putInt("MaxInsert", maxInsert);
		tag.putInt("CooldownTicksLeft", cooldownTicksLeft);
	}
}
