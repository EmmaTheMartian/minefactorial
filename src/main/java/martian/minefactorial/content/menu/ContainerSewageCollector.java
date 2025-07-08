package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.husbandry.BlockSewageCollectorBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import top.girlkisser.lazuli.api.menu.AbstractBlockEntityContainer;

//TODO: Sewage as a recipe, i.e, certain mobs have certain sewage fluids.
public class ContainerSewageCollector extends AbstractBlockEntityContainer<BlockSewageCollectorBE> {
    public final int capacity;
    public int fluidAmount;
    public int cooldownTicks;

    public ContainerSewageCollector(int containerId, Inventory playerInventory, BlockPos pos) {
        super(MFMenuTypes.SEWAGE_COLLECTOR.get(), MFBlocks.SEWAGE_COLLECTOR.get(), 0, containerId, playerInventory, pos);

        this.capacity = this.blockEntity.getTank().getCapacity();

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ContainerSewageCollector.this.blockEntity.cooldownTicks;
            }

            @Override
            public void set(int value) {
                cooldownTicks = value;
            }
        });

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return ContainerSewageCollector.this.blockEntity.getTank().getFluid().getAmount();
            }

            @Override
            public void set(int value) {
                fluidAmount = value;
            }
        });

        addPlayerInventorySlots(playerInventory, 8, 84);
    }
}
