package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockBreakerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerBreaker extends AbstractMachineContainer<BlockBreakerBE> {
	public ContainerBreaker(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.BREAKER.get(), MFBlocks.BREAKER.get(), BlockBreakerBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addSlotRange(this.blockEntity, 0, 44, 47, BlockBreakerBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
