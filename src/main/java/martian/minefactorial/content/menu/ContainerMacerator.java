package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockMaceratorBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ContainerMacerator extends AbstractMachineContainer<BlockMaceratorBE> {
	public ContainerMacerator(int containerId, Inventory playerInventory, BlockPos pos) {
		super(
				MFMenuTypes.MACERATOR.get(),
				MFBlocks.MACERATOR.get(),
				BlockMaceratorBE.INPUT_SLOTS + BlockMaceratorBE.OUTPUT_SLOTS,
				containerId,
				playerInventory,
				pos
		);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlot(new Slot(this.blockEntity, 0, 44, 47)); // Add input slot
		addOutputSlotBox(this.blockEntity, 1, 98, 38, 2, 2, 18, 18); // Add output slots

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
