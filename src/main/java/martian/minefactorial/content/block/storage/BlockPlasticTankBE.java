package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.block.AbstractSingleTankBE;
import top.girlkisser.lazuli.api.block.ISingleTankBE;

public class BlockPlasticTankBE extends AbstractSingleTankBE implements ISingleTankBE {
	public BlockPlasticTankBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PLASTIC_TANK.get(), 6000, pos, blockState);
	}
}
