package martian.minefactorial.foundation.block;

import martian.minefactorial.foundation.energy.ProtectiveEnergyStorage;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IEnergyBE extends IBE {
	ProtectiveEnergyStorage getEnergyStorage();

	/** Energy capacity of the machine */
	default int getMaxEnergy() {
		return 1600;
	}

	/** Maximum energy extractable per tick for the machine */
	default int getMaxEnergyExtract() {
		return 32;
	}

	/** Maximum energy receivable per tick for the machine */
	default int getMaxEnergyReceive() {
		return 32;
	}

	default int getEnergyStored() {
		return getEnergyStorage().getEnergyStored();
	}

	default boolean canDistributeInDirection(Direction direction) {
		return true;
	}

	default void distributePower() {
		for (Direction direction : Direction.values()) {
			if (!canDistributeInDirection(direction)) {
				continue;
			}

			if (getEnergyStorage().getEnergyStored() <= 0) {
				return;
			}

			IEnergyStorage energy = getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(direction), direction);
			if (energy != null && energy.canReceive()) {
				int received = energy.receiveEnergy(Math.min(getEnergyStorage().getEnergyStored(), getMaxEnergyExtract()), false);
				getEnergyStorage().forceExtractEnergy(received, false);
				setChanged();
			}
		}
	}
}
