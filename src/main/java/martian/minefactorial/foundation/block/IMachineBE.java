package martian.minefactorial.foundation.block;

public interface IMachineBE extends IEnergyBE {
	/** The amount of ticks the machine must wait until checking for available work again */
	default int getIdleTime() {
		return 20;
	}

	/** The amount of work required to perform an action */
	default int getMaxWork() {
		return 20;
	}

	/** The amount of energy to consume each time the machine works */
	default int getEnergyPerWork() {
		return 32;
	}
}
