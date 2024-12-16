package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockEjectorBE extends AbstractInventoryBE {
	public static final int SLOTS = 9;

	public int minStackSizeForEject = 1;

	public BlockEjectorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.EJECTOR.get(), SLOTS, pos, blockState);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("MinStackSizeForEject")) {
			minStackSizeForEject = tag.getInt("MinStackSizeForEject");
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		tag.putInt("MinStackSizeForEject", minStackSizeForEject);
	}
}
