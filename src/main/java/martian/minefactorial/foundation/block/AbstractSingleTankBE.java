package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class AbstractSingleTankBE extends BlockEntity implements ISingleTankBE {
	private final FluidTank tank;

	public AbstractSingleTankBE(BlockEntityType<?> type, int capacity, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		this.tank = new FluidTank(capacity) {
			@Override
			public void onContentsChanged() {
				AbstractSingleTankBE.this.setChanged();
			}
		};
	}

	@Override
	public FluidTank getTank() {
		return tank;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		CompoundTag tankTag = new CompoundTag();
		tank.writeToNBT(registries, tankTag);
		tag.put("Tank", tankTag);
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("Tank"))
			tank.readFromNBT(registries, tag.getCompound("Tank"));
	}
}
