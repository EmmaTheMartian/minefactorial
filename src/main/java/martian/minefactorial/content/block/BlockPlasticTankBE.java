package martian.minefactorial.content.block;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.ISingleTankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BlockPlasticTankBE extends BlockEntity implements ISingleTankBE {
	private final FluidTank tank = new FluidTank(6000) {
		@Override
		protected void onContentsChanged() {
			setChanged();
		}
	};

	public BlockPlasticTankBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PLASTIC_TANK.get(), pos, blockState);
	}

	@Override
	public FluidTank getTank() {
		return tank;
	}
}
