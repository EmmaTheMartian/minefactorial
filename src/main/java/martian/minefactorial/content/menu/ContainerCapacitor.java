package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.BlockCapacitorBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerCapacitor extends AbstractMachineContainer<BlockCapacitorBE> {
	public ContainerCapacitor(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.CAPACITOR_CONTAINER.get(), MFBlocks.CAPACITOR.get(), BlockCapacitorBE.SLOT_COUNT, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);

		addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 80, 35));

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
