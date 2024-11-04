package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.storage.BlockCapacitorBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerCapacitor extends AbstractMachineContainer<BlockCapacitorBE> {
	public ContainerCapacitor(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.CAPACITOR.get(), MFBlocks.CAPACITOR.get(), BlockCapacitorBE.SLOT_COUNT, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);

		addSlot(new SlotItemHandler(this.blockEntity.getInventory(), 0, 80, 35));

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
