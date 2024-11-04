package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.power.BlockSteamBoilerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerSteamBoiler extends AbstractMachineContainer<BlockSteamBoilerBE> {
	public final int waterCapacity, steamCapacity;
	protected int waterMillibuckets, steamMillibuckets, burnTicksLeft, totalBurnTicksForFuel;

	public ContainerSteamBoiler(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.STEAM_BOILER.get(), MFBlocks.STEAM_BOILER.get(), BlockSteamBoilerBE.SLOT_COUNT, containerId, playerInventory, pos);

		assert this.blockEntity != null;

		this.waterCapacity = this.blockEntity.getWaterTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSteamBoiler.this.blockEntity.getWaterTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerSteamBoiler.this.waterMillibuckets = amount;
			}
		});

		this.steamCapacity = this.blockEntity.getSteamTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSteamBoiler.this.blockEntity.getSteamTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerSteamBoiler.this.steamMillibuckets = amount;
			}
		});

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSteamBoiler.this.blockEntity.burnTicksLeft;
			}

			@Override
			public void set(int value) {
				ContainerSteamBoiler.this.burnTicksLeft = value;
			}
		});

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSteamBoiler.this.blockEntity.totalBurnTicksForFuel;
			}

			@Override
			public void set(int value) {
				ContainerSteamBoiler.this.totalBurnTicksForFuel = value;
			}
		});

		addSlot(new SlotItemHandler(this.blockEntity.getInventory(), 0, 35, 53));

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getWaterMillibuckets() {
		return waterMillibuckets;
	}

	public int getSteamMillibuckets() {
		return steamMillibuckets;
	}

	public int getBurnTicksLeft() {
		return burnTicksLeft;
	}

	public int getTotalBurnTicksForFuel() {
		return totalBurnTicksForFuel;
	}
}
