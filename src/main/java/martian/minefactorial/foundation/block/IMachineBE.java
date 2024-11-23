package martian.minefactorial.foundation.block;

import net.minecraft.server.level.ServerLevel;

public interface IMachineBE extends IEnergyBE {
	/**
	 * Called when idle runs out to check for new work. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return If work is available.
	 */
	boolean checkForWork(ServerLevel level);

	/**
	 * Called when the machine should perform its task. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 */
	default void doWork(ServerLevel level) { }

	/**
	 * Called for every tick that the machine is working. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return {@code true} if working should be cancelled, {@code false} otherwise.
	 */
	default boolean onWorkTick(ServerLevel level) {
		return false;
	}

	/**
	 * Called immediately after the machine leaves its idle state and before {@code doWork}
	 * or {@code doWorkTick} are invoked.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return {@code true} to return to the idle state, {@code false} otherwise.
	 */
	default boolean afterIdle(ServerLevel level) {
		return false;
	}

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
