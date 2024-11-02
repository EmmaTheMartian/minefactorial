package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInventoryBE extends BlockEntity implements IInventoryBE {
	protected final ItemStackHandler inventory;
	protected final int slots;

	public AbstractInventoryBE(BlockEntityType<?> type, int slots, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		this.slots = slots;
		this.inventory = makeItemStackHandler();
	}

	protected ItemStackHandler makeItemStackHandler() {
		return new ItemStackHandler(slots) {
			@Override
			public void onContentsChanged(int slot) {
				AbstractInventoryBE.this.setChanged();
			}
		};
	}

	@Override
	public ItemStackHandler getInventory() {
		return inventory;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Inventory", inventory.serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		if (tag.contains("Inventory"))
			inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
	}
}
