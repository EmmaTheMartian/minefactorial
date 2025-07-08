package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.block.AbstractCapacitorBE;

public class BlockCapacitorBE extends AbstractCapacitorBE {
	public BlockCapacitorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CAPACITOR.get(), pos, blockState);
	}
}
