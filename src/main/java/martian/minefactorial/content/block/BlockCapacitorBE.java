package martian.minefactorial.content.block;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractEnergyBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockCapacitorBE extends AbstractEnergyBE implements ITickableBE, IInventoryBE {
	protected final ItemStackHandler itemHandler = new ItemStackHandler();

	public static final int SLOT_COUNT = 1;

	public BlockCapacitorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CAPACITOR.get(), pos, blockState);
	}

	public ItemStack getHeldStack() {
		return itemHandler.getStackInSlot(0);
	}

	public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
		return direction == Direction.UP || direction == null ?
				getEnergyStorage().getReceiveOnly() :
				getEnergyStorage().getExtractOnly();
	}

	@Override
	public ItemStackHandler getInventory() {
		return itemHandler;
	}

	@Override
	public boolean canDistributeInDirection(Direction direction) {
		return direction != Direction.UP;
	}

	@Override
	public void serverTick() {
		if (getEnergyStored() <= 0) {
			return;
		}

		// Attempt to charge the held item, if it exists
		IEnergyStorage itemEnergy = getHeldStack().getCapability(Capabilities.EnergyStorage.ITEM);
		if (itemEnergy != null && itemEnergy.canReceive()) {
			int received = itemEnergy.receiveEnergy(Math.min(getEnergyStored(), getMaxEnergyExtract()), false);
			getEnergyStorage().forceExtractEnergy(received, false);
			setChanged();
		}

		// Attempt to charge adjacent blocks
		if (getEnergyStored() > 0) {
			this.distributePower();
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Item", itemHandler.serializeNBT(registries));
	}

	@Override
	@ParametersAreNonnullByDefault
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("Item"))
			itemHandler.deserializeNBT(registries, tag.getCompound("Item"));
	}
}
