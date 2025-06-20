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
	public boolean autoEject = false;

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

	public abstract boolean checkForWork(ServerLevel level);

	public abstract void doWork(ServerLevel level);

	public boolean hasRedstoneControls() {
		return true;
	}

	/**
	 * Called when the machine starts working. This can be used to change the BlockState
	 * for animation purposes.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 */
	public void onWorkStart(ServerLevel level) { }

	/**
	 * Called when the machine stops working (whether successful or not). This can be
	 * used to change the BlockState for animation purposes.
	 * @param level The server level. Null checks are performed before this is called, so
	 *              you do not have to worry about checking it manually.
	 */
	public void onWorkStop(ServerLevel level) { }

	@Override
	public void serverTick(ServerLevel level) {
		// If there's a redstone signal coming into the block, we will skip everything here.
		if (hasRedstoneControls() && level.hasNeighborSignal(getBlockPos())) {
			return;
		}

		// Eject items, if there are any
		if (this instanceof IInventoryBE<?> inventoryBE && inventoryBE.shouldEjectItems() && !inventoryBE.isEmpty()) {
			IInventoryBE.ejectFrom(inventoryBE, 64);
		}

		// Check if idle timer has expired
		if (isIdle) {
			if (++currentIdleTime >= getIdleTime()) {
				currentIdleTime = 0;
				if (this.getEnergyStored() >= this.getEnergyPerWork() && checkForWork(level) && !afterIdle(level)) {
					isIdle = false;
					onWorkStart(level);
				}
			}
			return;
		}

		if (onWorkTick(level)) {
			isIdle = true;
			currentIdleTime = 0;
			currentWork = 0;
			onWorkStop(level);
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
