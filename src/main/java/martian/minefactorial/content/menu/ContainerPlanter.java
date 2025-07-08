package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.farming.BlockPlanterBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerPlanter extends AbstractMachineContainer<BlockPlanterBE> {
	public ContainerPlanter(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.PLANTER.get(), MFBlocks.PLANTER.get(), BlockPlanterBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlotBox(this.blockEntity, 0, 62, 38, 3, 3, 18, 18);

		addPlayerInventorySlots(playerInventory, 8, 105);
	}
}
