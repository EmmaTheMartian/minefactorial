package martian.minefactorial.content.menu;

import martian.minefactorial.api.menu.AbstractMachineContainer;
import martian.minefactorial.content.block.machinery.husbandry.BlockMobRouterBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.content.registry.MFMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerMobRouter extends AbstractMachineContainer<BlockMobRouterBE> {
    public ContainerMobRouter(int containerId, Inventory playerInventory, BlockPos pos) {
        super(MFMenuTypes.MOB_ROUTER.get(), MFBlocks.MOB_ROUTER.get(), BlockMobRouterBE.SLOTS, containerId, playerInventory, pos);

        addEnergySlot(this.blockEntity);
        addWorkSlot(this.blockEntity);
        addIdleSlot(this.blockEntity);

        addSlot(new Slot(this.blockEntity, 0, 80, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.has(MFDataComponents.SAFARI_NET_DATA);
            }
        });

        addPlayerInventorySlots(playerInventory, 8, 84);
    }
}
