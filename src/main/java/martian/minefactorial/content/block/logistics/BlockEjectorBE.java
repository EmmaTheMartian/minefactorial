package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEjectorBE extends AbstractInventoryBE {
	public BlockEjectorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.EJECTOR.get(), BlockEjector.SLOTS, pos, blockState);
	}
}
