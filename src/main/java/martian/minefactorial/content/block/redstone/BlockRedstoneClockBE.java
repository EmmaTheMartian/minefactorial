package martian.minefactorial.content.block.redstone;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockRedstoneClockBE extends BlockEntity implements ITickableBE {
	public int toggleTimeTicks = 20, ticksToNextToggle = toggleTimeTicks;

	public BlockRedstoneClockBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.REDSTONE_CLOCK.get(), pos, blockState);
	}

	@Override
	public void serverTick() {
		if (--ticksToNextToggle <= 0) {
			assert level != null;
			level.setBlockAndUpdate(worldPosition, getBlockState()
					.setValue(BlockRedstoneClock.POWERED, !getBlockState().getValue(BlockRedstoneClock.POWERED)));
			ticksToNextToggle = toggleTimeTicks;
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("ToggleTimeTicks")) {
			toggleTimeTicks = tag.getInt("ToggleTimeTicks");
		}

		if (tag.contains("TicksToNextToggle")) {
			ticksToNextToggle = tag.getInt("TicksToNextToggle");
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		tag.putInt("ToggleTimeTicks", toggleTimeTicks);
		tag.putInt("TicksToNextToggle", ticksToNextToggle);
	}
}
