package martian.minefactorial.foundation.energy;

import net.minecraft.util.Mth;

public class ProtectiveEnergyStorage extends MFEnergyStorage {
	public ProtectiveEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public int forceReceiveEnergy(int toReceive, boolean simulate) {
		if (toReceive > 0) {
			int energyReceived = Mth.clamp(this.capacity - this.energy, 0, toReceive);
			if (!simulate) {
				this.energy += energyReceived;
			}

			return energyReceived;
		} else {
			return 0;
		}
	}

	public int forceExtractEnergy(int toExtract, boolean simulate) {
		if (toExtract > 0) {
			int energyExtracted = Math.min(this.energy, toExtract);
			if (!simulate) {
				this.energy -= energyExtracted;
			}

			return energyExtracted;
		} else {
			return 0;
		}
	}
}
