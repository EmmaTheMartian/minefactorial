package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.farming.BlockHarvesterBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerHarvester extends AbstractMachineContainer<BlockHarvesterBE> {
	public ContainerHarvester(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.HARVESTER.get(), MFBlocks.HARVESTER.get(), BlockHarvesterBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlotRange(this.blockEntity, 0, 44, 47, BlockHarvesterBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
