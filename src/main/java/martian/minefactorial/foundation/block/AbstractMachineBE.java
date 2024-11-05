package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMachineBE
		extends AbstractEnergyBE
		implements IMachineBE, ITickableBE
{
	private int currentWork = 0;
	private int currentIdleTime = 0;
	private boolean isIdle = true;

	public AbstractMachineBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public int getCurrentWork() {
		return currentWork;
	}

	public int getCurrentIdleTime() {
		return currentIdleTime;
	}

	/**
	 * Called when idle runs out to check for new work. This is only executed on the server.
	 * @return If work is available.
	 */
	public abstract boolean checkForWork();

	/**
	 * Called when the machine should perform its task. This is only executed on the server.
	 */
	public abstract void doWork();

	@Override
	public void serverTick() {
		// Eject items, if there are any
		if (this instanceof IInventoryBE inventoryBE && inventoryBE.shouldEjectItems() && !inventoryBE.isEmpty()) {
			IInventoryBE.ejectFrom(inventoryBE, 64);
		}

		// Check if idle timer has expired
		if (isIdle) {
			if (++currentIdleTime >= getIdleTime()) {
				currentIdleTime = 0;
				if (this.getEnergyStored() >= this.getEnergyPerWork() && checkForWork()) {
					isIdle = false;
				}
			}
			return;
		}

		// Do work
		if (++currentWork >= getMaxWork()) {
			// Check for work just once more to be sure that everything is still valid
			if (!checkForWork()) {
				return;
			}

			// If we do not have enough energy, immediately go idle
			if (this.getEnergyStored() < this.getEnergyPerWork()) {
				isIdle = true;
				currentIdleTime = 0;
				currentWork = 0;
				return;
			}

			this.getEnergyStorage().forceExtractEnergy(getEnergyPerWork(), false);
			currentWork = 0;
			doWork();

			if (!checkForWork()) {
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
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("Work"))
			this.currentWork = tag.getInt("Work");

		if (tag.contains("Idle"))
			this.currentIdleTime = tag.getInt("Idle");
	}
}
