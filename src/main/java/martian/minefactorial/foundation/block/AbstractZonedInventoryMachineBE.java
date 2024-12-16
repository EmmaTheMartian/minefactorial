package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;

public abstract class AbstractZonedInventoryMachineBE
		extends AbstractInventoryMachineBE
		implements IZonedBE
{
	protected Lazy<AABB> cachedWorkZone = Lazy.of(this::getWorkZone);

	public AbstractZonedInventoryMachineBE(BlockEntityType<?> type, int slots, BlockPos pos, BlockState blockState) {
		super(type, slots, pos, blockState);
	}

	@Override
	public Lazy<AABB> getCachedWorkZone() {
		return cachedWorkZone;
	}
}
