package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockSmasherBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;

public class ContainerSmasher extends AbstractMachineContainer<BlockSmasherBE> {
	public final int tankCapacity;
	public int fluidAmount;

	private final DataSlot fortuneLevelSlot;

	public ContainerSmasher(int containerId, Inventory playerInventory, BlockPos pos) {
		super(
				MFMenuTypes.SMASHER.get(),
				MFBlocks.SMASHER.get(),
				BlockSmasherBE.INPUT_SLOTS + BlockSmasherBE.OUTPUT_SLOTS,
				containerId,
				playerInventory,
				pos
		);

		this.tankCapacity = this.blockEntity.getTank().getCapacity();

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSmasher.this.blockEntity.getTank().getFluid().getAmount();
			}

			@Override
			public void set(int value) {
				fluidAmount = value;
			}
		});

		this.fortuneLevelSlot = new DataSlot() {
			@Override
			public int get() {
				return ContainerSmasher.this.blockEntity.fortuneLevel;
			}

			@Override
			public void set(int value) {
				ContainerSmasher.this.blockEntity.fortuneLevel = value;
			}
		};

		addDataSlot(fortuneLevelSlot);

		addSlot(new Slot(this.blockEntity, 0, 44, 47)); // Add input slot
		addOutputSlotBox(this.blockEntity, 1, 98, 38, 2, 2, 18, 18); // Add output slots

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public void updateFortuneLevel(int value) {
		this.fortuneLevelSlot.set(value);
	}

	public int getFluidAmount() {
		return fluidAmount;
	}

	public int getFortuneLevel() {
		return this.blockEntity.fortuneLevel;
	}
}
