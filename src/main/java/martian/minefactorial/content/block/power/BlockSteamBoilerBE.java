package martian.minefactorial.content.block.power;

import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.block.IInventoryBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BlockSteamBoilerBE extends BlockEntity implements ITickableBE, IInventoryBE {
	public static final int SLOT_COUNT = 1;

	private final FluidTank waterTank = new FluidTank(2000, fluidStack -> fluidStack.is(Fluids.WATER)) {
		@Override
		protected void onContentsChanged() {
			setChanged();
		}
	};

	private final FluidTank steamTank = new FluidTank(2000, fluidStack -> fluidStack.is(MFTags.STEAM)) {
		@Override
		protected void onContentsChanged() {
			setChanged();
		}
	};

	protected final ItemStackHandler itemHandler = new ItemStackHandler();

	public int burnTicksLeft = 0, totalBurnTicksForFuel = 0;

	public BlockSteamBoilerBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.STEAM_BOILER.get(), pos, blockState);
	}

	public FluidTank getWaterTank() {
		return waterTank;
	}

	public FluidTank getSteamTank() {
		return steamTank;
	}

	@Override
	public ItemStackHandler getInventory() {
		return itemHandler;
	}

	public ItemStack getHeldStack() {
		return itemHandler.getStackInSlot(0);
	}

	public void serverTick() {
		if (burnTicksLeft <= 0) {
			// Check for new burnable items
			int time = getHeldStack().getBurnTime(RecipeType.SMELTING);
			if (time > 0) {
				totalBurnTicksForFuel = time;
				burnTicksLeft = time;
				getHeldStack().shrink(1);
			}
		} else {
			burnTicksLeft--;
		}

		if (
				burnTicksLeft > 0 &&
				getWaterTank().getFluidAmount() > 0 &&
				getSteamTank().getFluidAmount() < getSteamTank().getCapacity()
		) {
			getWaterTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
			getSteamTank().fill(new FluidStack(MFFluids.STEAM, 1), IFluidHandler.FluidAction.EXECUTE);
		}

		// Automatically push steam up
		if (getSteamTank().getFluidAmount() > 0) {
			assert level != null;
			IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition.above(), Direction.DOWN);
			if (fluidHandler != null) {
				fluidHandler.fill(new FluidStack(MFFluids.STEAM, 1), IFluidHandler.FluidAction.EXECUTE);
				getSteamTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
			}
		}

		setChanged();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		CompoundTag water = new CompoundTag();
		getWaterTank().writeToNBT(registries, water);
		tag.put("WaterTank", water);

		CompoundTag steam = new CompoundTag();
		getSteamTank().writeToNBT(registries, steam);
		tag.put("SteamTank", steam);

		tag.put("Item", itemHandler.serializeNBT(registries));

		tag.putInt("BurnTicksLeft", burnTicksLeft);
		tag.putInt("TotalBurnTicksForFuel", totalBurnTicksForFuel);
	}

	@Override
	protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("WaterTank"))
			getWaterTank().readFromNBT(registries, tag.getCompound("WaterTank"));
		if (tag.contains("SteamTank"))
			getSteamTank().readFromNBT(registries, tag.getCompound("SteamTank"));
		if (tag.contains("Item"))
			itemHandler.deserializeNBT(registries, tag.getCompound("Item"));
		if (tag.contains("BurnTicksLeft"))
			burnTicksLeft = tag.getInt("BurnTicksLeft");
		if (tag.contains("TotalBurnTicksForFuel"))
			totalBurnTicksForFuel = tag.getInt("TotalBurnTicksForFuel");
	}

	public IFluidHandler getTankForSide(Direction direction) {
		if (direction == Direction.UP) {
			return steamTank;
		} else {
			return waterTank;
		}
	}
}
