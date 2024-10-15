package martian.minefactorial.foundation.block;

public interface IEnergyBE {
	/** Energy capacity of the machine */
	default int getMaxEnergy() {
		return 1600;
	}

	/** Maximum energy extractable per tick for the machine */
	default int getMaxExtract() {
		return 32;
	}

	/** Maximum energy receivable per tick for the machine */
	default int getMaxReceive() {
		return 32;
	}

	int getEnergyStored();
}
