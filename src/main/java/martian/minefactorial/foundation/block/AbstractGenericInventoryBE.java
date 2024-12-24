package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

/**
 * {@link AbstractInventoryBE} but allowing for any valid item handler.
 * {@see BlockItemRouterBE} for an example use-case.
 * @param <T>
 */
public abstract class AbstractGenericInventoryBE<T extends IItemHandler & IItemHandlerModifiable & INBTSerializable<CompoundTag>>
		extends BlockEntity
		implements IInventoryBE<T>
{
	protected final T inventory;
	protected final int slots;

	public AbstractGenericInventoryBE(BlockEntityType<?> type, int slots, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		this.slots = slots;
		this.inventory = makeItemStackHandler();
	}

	protected abstract T makeItemStackHandler();

	@Override
	public T getInventory() {
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
