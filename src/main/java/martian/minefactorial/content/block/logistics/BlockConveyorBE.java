package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.api.block.AbstractConveyorBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockConveyorBE extends AbstractConveyorBE {
	public BlockConveyorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CONVEYOR.get(), pos, blockState);
	}
}
