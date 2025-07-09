package martian.minefactorial.content.menu;

import martian.minefactorial.api.menu.AbstractMachineContainer;
import martian.minefactorial.content.block.machinery.husbandry.BlockChronotyperBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerChronotyper extends AbstractMachineContainer<BlockChronotyperBE> {
    private final DataSlot moveStateSlot;

    public ContainerChronotyper(int containerId, Inventory playerInventory, BlockPos pos) {
        super(MFMenuTypes.CHRONOTYPER.get(), MFBlocks.CHRONOTYPER.get(), 0, containerId, playerInventory, pos);

        addEnergySlot(this.blockEntity);
        addWorkSlot(this.blockEntity);
        addIdleSlot(this.blockEntity);

        this.moveStateSlot = new DataSlot() {
            @Override
            public int get() {
                return blockEntity.state.ordinal();
            }

            @Override
            public void set(int value) {
                blockEntity.state = BlockChronotyperBE.MoveState.values()[value];
                blockEntity.setChanged();
            }
        };
        addDataSlot(moveStateSlot);

        addPlayerInventorySlots(playerInventory, 8, 84);
    }

    public void setMoveState(BlockChronotyperBE.MoveState state) {
        this.moveStateSlot.set(state.ordinal());
    }

    public BlockChronotyperBE.MoveState getMoveState() {
        return BlockChronotyperBE.MoveState.values()[this.moveStateSlot.get()];
    }
}
