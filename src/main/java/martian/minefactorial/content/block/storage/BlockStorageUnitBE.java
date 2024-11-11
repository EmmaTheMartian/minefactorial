package martian.minefactorial.content.block.storage;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BlockStorageUnitBE extends AbstractInventoryBE {
	public static final int SLOTS = 54; // The size of a double chest

	public BlockStorageUnitBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.STORAGE_UNIT.get(), SLOTS, pos, blockState);
	}

	protected ItemStackHandler makeItemStackHandler() {
		return new ItemStackHandler(SLOTS) {
			@Override
			public void onContentsChanged(int slot) {
				BlockStorageUnitBE.this.setChanged();
			}
		};
	}
}
