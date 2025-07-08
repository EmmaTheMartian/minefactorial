package martian.minefactorial.content.block.redstone;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.block.ITickableBE;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockRedstoneClockBE extends BlockEntity implements ITickableBE {
	public int toggleTimeTicks = 20, ticksToNextToggle = toggleTimeTicks;
	public boolean locked = false;

	public BlockRedstoneClockBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.REDSTONE_CLOCK.get(), pos, blockState);
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (!locked && --ticksToNextToggle <= 0) {
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

		if (tag.contains("Locked")) {
			locked = tag.getBoolean("Locked");
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		tag.putInt("ToggleTimeTicks", toggleTimeTicks);
		tag.putInt("TicksToNextToggle", ticksToNextToggle);
		tag.putBoolean("Locked", locked);
	}
}
