package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;

public abstract class AbstractZonedSingleTankAndInventoryMachineBE
		extends AbstractSingleTankAndInventoryMachineBE
		implements IZonedBE
{
	protected Lazy<AABB> cachedWorkZone = Lazy.of(this::getWorkZone);

	public AbstractZonedSingleTankAndInventoryMachineBE(BlockEntityType<?> type, int tankCapacity, int slots, BlockPos pos, BlockState blockState) {
		super(type, tankCapacity, slots, pos, blockState);
	}

	@Override
	public Lazy<AABB> getCachedWorkZone() {
		return cachedWorkZone;
	}
}
