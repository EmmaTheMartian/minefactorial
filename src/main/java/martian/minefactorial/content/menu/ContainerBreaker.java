package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.BlockBreakerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;

public class ContainerBreaker extends AbstractMachineContainer<BlockBreakerBE> {
	public ContainerBreaker(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.BREAKER.get(), MFBlocks.BREAKER.get(), 0, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
