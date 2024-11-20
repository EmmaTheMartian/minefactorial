package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMachineBE
		extends AbstractEnergyBE
		implements IMachineBE, ITickableBE
{
	protected int currentWork = 0;
	protected int currentIdleTime = 0;
	protected boolean isIdle = true;

	/** If this machine should automatically eject its items to a container or into the world. */
	public boolean autoEject = true;

	public AbstractMachineBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	/** @return The amount of ticks that this machine has been working for */
	public int getCurrentWork() {
		return currentWork;
	}

	/** @return The amount of ticks that this machine has been idle for */
	public int getCurrentIdleTime() {
		return currentIdleTime;
	}

	/**
	 * Called when idle runs out to check for new work. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return If work is available.
	 */
	public abstract boolean checkForWork(ServerLevel level);

	/**
	 * Called immediately after the machine leaves its idle state and before {@code doWork}
	 * or {@code doWorkTick} are invoked.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return {@code true} to return to the idle state, {@code false} otherwise.
	 */
	public boolean afterIdle(ServerLevel level) {
		return false;
	}

	/**
	 * Called when the machine should perform its task. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 */
	public abstract void doWork(ServerLevel level);

	/**
	 * Called for every tick that the machine is working. This is only executed on the server.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 * @return {@code true} if working should be cancelled, {@code false} otherwise.
	 */
	public boolean onWorkTick(ServerLevel level) {
		return false;
	}

	@Override
	public void serverTick(ServerLevel level) {
		// Eject items, if there are any
		if (this instanceof IInventoryBE inventoryBE && inventoryBE.shouldEjectItems() && !inventoryBE.isEmpty()) {
			IInventoryBE.ejectFrom(inventoryBE, 64);
		}

		// Check if idle timer has expired
		if (isIdle) {
			if (++currentIdleTime >= getIdleTime()) {
				currentIdleTime = 0;
				if (this.getEnergyStored() >= this.getEnergyPerWork() && checkForWork(level) && !afterIdle(level)) {
					isIdle = false;
				}
			}
			return;
		}

		if (onWorkTick(level)) {
			isIdle = true;
			currentIdleTime = 0;
			currentWork = 0;
			return;
		}

		// Do work
		if (++currentWork >= getMaxWork()) {
			// Check for work just once more to be sure that everything is still valid
			// If we do not have enough energy, immediately go idle
			if (!checkForWork(level) || this.getEnergyStored() < this.getEnergyPerWork()) {
				isIdle = true;
				currentIdleTime = 0;
				currentWork = 0;
				return;
			}

			this.getEnergyStorage().forceExtractEnergy(getEnergyPerWork(), false);
			currentWork = 0;
			doWork(level);

			if (!checkForWork(level)) {
				isIdle = true;
				currentIdleTime = 0;
			}
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("Work", currentWork);
		tag.putInt("Idle", currentIdleTime);
		tag.putBoolean("AutoEject", autoEject);
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("Work"))
			this.currentWork = tag.getInt("Work");

		if (tag.contains("Idle"))
			this.currentIdleTime = tag.getInt("Idle");

		if (tag.contains("AutoEject"))
			this.autoEject = tag.getBoolean("AutoEject");
	}
}
