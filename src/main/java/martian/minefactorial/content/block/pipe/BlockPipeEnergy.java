package martian.minefactorial.content.block.pipe;

import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.foundation.block.ITickableBE;
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockPipeEnergy extends AbstractPipeBlock<IEnergyStorage, BlockPipeEnergyBE> implements ITickableBE {
	public BlockPipeEnergy(Properties properties) {
		super(BlockPipeEnergyBE::new, MFBlocks.ENERGY_PIPE, Capabilities.EnergyStorage.BLOCK, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockPipeEnergyBE pipe) {
			pipe.setChanged();
		}
	}
}
