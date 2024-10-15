package martian.minefactorial.foundation.block;

import martian.minefactorial.foundation.energy.ProtectiveEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractEnergyBE extends BlockEntity implements IEnergyBE {
	private final ProtectiveEnergyStorage energyStorage = makeEnergyStorage();

	public AbstractEnergyBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	protected ProtectiveEnergyStorage makeEnergyStorage() {
		return new ProtectiveEnergyStorage(getMaxEnergy(), getMaxReceive(), getMaxExtract());
	}

	public ProtectiveEnergyStorage getEnergyStorage() {
		return energyStorage;
	}

	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	public void distributePower() {
		assert level != null;

		for (Direction direction : Direction.values()) {
			if (getEnergyStorage().getEnergyStored() <= 0) {
				return;
			}

			IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(direction), null);
			if (energy != null && energy.canReceive()) {
				int received = energy.receiveEnergy(Math.min(getEnergyStorage().getEnergyStored(), getMaxExtract()), false);
				getEnergyStorage().extractEnergy(received, false);
				setChanged();
			}
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Energy", getEnergyStorage().serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("Energy"))
			this.getEnergyStorage().deserializeNBT(registries, Objects.requireNonNull(tag.get("Energy")));
	}
}
