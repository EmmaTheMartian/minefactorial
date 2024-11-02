package martian.minefactorial.content.block.pipe;

import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockPipeFluid extends AbstractPipeBlock<IFluidHandler, BlockPipeFluidBE> {
	public BlockPipeFluid(Properties properties) {
		super(BlockPipeFluidBE::new, MFBlocks.FLUID_PIPE, Capabilities.FluidHandler.BLOCK, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockPipeFluidBE pipe) {
			pipe.setChanged();
		}
	}
}
