package martian.minefactorial.content.block.storage;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockStorageUnit extends AbstractBlockWithEntity<BlockStorageUnitBE> {
	public BlockStorageUnit(Properties properties) {
		super(BlockStorageUnitBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@ParametersAreNonnullByDefault
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}
}
