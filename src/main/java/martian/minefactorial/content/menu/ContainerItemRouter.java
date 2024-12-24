package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.logistics.BlockItemRouterBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractBlockEntityContainer;
import martian.minefactorial.foundation.menu.GhostSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerItemRouter extends AbstractBlockEntityContainer<BlockItemRouterBE> {
	public ContainerItemRouter(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.ITEM_ROUTER.get(), MFBlocks.ITEM_ROUTER.get(), BlockItemRouterBE.SLOTS, containerId, playerInventory, pos);

		addSlotBox(this.blockEntity, 0, 8, 18, 9, 6, 18, 18, GhostSlot::new);

		addPlayerInventorySlots(playerInventory, 8, 140);
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
		return ItemStack.EMPTY;
	}
}
