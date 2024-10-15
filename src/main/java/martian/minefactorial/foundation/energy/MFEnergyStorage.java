package martian.minefactorial.foundation.energy;

import net.neoforged.neoforge.energy.EnergyStorage;

public class MFEnergyStorage extends EnergyStorage implements ISerializableEnergyStorage {
	public MFEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
}
