package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockPlacerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerPlacer extends AbstractMachineContainer<BlockPlacerBE> {
	public ContainerPlacer(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.PLACER.get(), MFBlocks.PLACER.get(), BlockPlacerBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlotRange(this.blockEntity, 0, 80, 47, BlockPlacerBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
