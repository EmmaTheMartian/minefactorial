package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractCapacitorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCapacitorBE extends AbstractCapacitorBE {
	public BlockCapacitorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CAPACITOR.get(), pos, blockState);
	}
}
