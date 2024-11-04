package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockMobGrinderBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerMobGrinder extends AbstractMachineContainer<BlockMobGrinderBE> {
	public final int essenceCapacity;
	protected int essenceMillibuckets;

	public ContainerMobGrinder(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.MOB_GRINDER.get(), MFBlocks.MOB_GRINDER.get(), BlockMobGrinderBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		this.essenceCapacity = this.blockEntity.getTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerMobGrinder.this.blockEntity.getTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerMobGrinder.this.essenceMillibuckets = amount;
			}
		});

		addSlotRange(this.blockEntity, 0, 44, 47, BlockMobGrinderBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getEssenceMillibuckets() {
		return essenceMillibuckets;
	}
}
