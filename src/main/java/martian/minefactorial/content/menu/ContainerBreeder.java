package martian.minefactorial.content.menu;

import martian.minefactorial.api.menu.AbstractMachineContainer;
import martian.minefactorial.content.block.machinery.BlockBreakerBE;
import martian.minefactorial.content.block.machinery.husbandry.BlockBreederBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerBreeder extends AbstractMachineContainer<BlockBreederBE> {
	public ContainerBreeder(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.BREEDER.get(), MFBlocks.BREEDER.get(), BlockBreederBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlotRange(this.blockEntity, 0, 44, 47, BlockBreederBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
