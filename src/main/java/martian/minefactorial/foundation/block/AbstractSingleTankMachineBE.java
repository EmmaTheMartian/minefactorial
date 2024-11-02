package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSingleTankMachineBE extends AbstractMachineBE implements ISingleTankBE {
	private final FluidTank tank;
	protected final int capacity;

	public AbstractSingleTankMachineBE(BlockEntityType<?> type, int capacity, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		this.capacity = capacity;
		this.tank = makeFluidTank();
	}

	protected boolean validate(FluidStack stack) {
		return true;
	}

	protected FluidTank makeFluidTank() {
		return new FluidTank(capacity, this::validate) {
			@Override
			public void onContentsChanged() {
				AbstractSingleTankMachineBE.this.setChanged();
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
