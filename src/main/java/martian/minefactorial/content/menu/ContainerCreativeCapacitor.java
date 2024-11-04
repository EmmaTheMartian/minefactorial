package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.storage.BlockCreativeCapacitorBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerCreativeCapacitor extends AbstractMachineContainer<BlockCreativeCapacitorBE> {
	public ContainerCreativeCapacitor(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.CREATIVE_CAPACITOR.get(), MFBlocks.CREATIVE_CAPACITOR.get(), BlockCreativeCapacitorBE.SLOT_COUNT, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);

		addSlot(new SlotItemHandler(this.blockEntity.getInventory(), 0, 80, 35));

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
