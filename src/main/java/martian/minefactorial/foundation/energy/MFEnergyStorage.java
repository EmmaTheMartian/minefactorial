package martian.minefactorial.foundation.energy;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class MFEnergyStorage extends EnergyStorage implements ISerializableEnergyStorage {
	public MFEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	protected void onChange() {}

	@Override
	public int receiveEnergy(int i, boolean bl) {
		int amount = super.receiveEnergy(i, bl);
		if (amount != 0) onChange();
		return amount;
	}

	@Override
	public int extractEnergy(int i, boolean bl) {
		int amount = super.extractEnergy(i, bl);
		if (amount != 0) onChange();
		return amount;
	}

	public IEnergyStorage getExtractOnly() {
		return new IEnergyStorage() {
			@Override
			public int receiveEnergy(int i, boolean bl) {
				return 0;
			}

			@Override
			public int extractEnergy(int i, boolean bl) {
				return MFEnergyStorage.this.extractEnergy(i, bl);
			}

			@Override
			public int getEnergyStored() {
				return MFEnergyStorage.this.getEnergyStored();
			}

			@Override
			public int getMaxEnergyStored() {
				return MFEnergyStorage.this.getMaxEnergyStored();
			}

			@Override
			public boolean canExtract() {
				return true;
			}

			@Override
			public boolean canReceive() {
				return false;
			}
		};
	}

	public IEnergyStorage getReceiveOnly() {
		return new IEnergyStorage() {
			@Override
			public int receiveEnergy(int i, boolean bl) {
				return MFEnergyStorage.this.receiveEnergy(i, bl);
			}

			@Override
			public int extractEnergy(int i, boolean bl) {
				return 0;
			}

			@Override
			public int getEnergyStored() {
				return MFEnergyStorage.this.getEnergyStored();
			}

			@Override
			public int getMaxEnergyStored() {
				return MFEnergyStorage.this.getMaxEnergyStored();
			}

			@Override
			public boolean canExtract() {
				return false;
			}

			@Override
			public boolean canReceive() {
				return true;
			}
		};
	}
}
