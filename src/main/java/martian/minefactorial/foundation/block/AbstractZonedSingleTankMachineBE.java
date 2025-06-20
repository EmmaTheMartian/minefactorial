package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;

public abstract class AbstractZonedSingleTankMachineBE
		extends AbstractSingleTankMachineBE
		implements IZonedBE
{
	protected final Lazy<AABB> cachedWorkZone = Lazy.of(this::getWorkZone);

	public AbstractZonedSingleTankMachineBE(BlockEntityType<?> type, int tankCapacity, BlockPos pos, BlockState blockState) {
		super(type, tankCapacity, pos, blockState);
	}

	@Override
	public Lazy<AABB> getCachedWorkZone() {
		return cachedWorkZone;
	}

	@Override
	public void setChanged() {
		this.invalidateWorkZone();
		super.setChanged();
	}
}
