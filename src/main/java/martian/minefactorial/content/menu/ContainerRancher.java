package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.husbandry.BlockMobGrinderBE;
import martian.minefactorial.content.block.machinery.husbandry.BlockRancherBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerRancher extends AbstractMachineContainer<BlockRancherBE> {
	public final int capacity;
	public int fluidStackId, fluidAmount;

	public ContainerRancher(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.RANCHER.get(), MFBlocks.RANCHER.get(), BlockRancherBE.SLOTS, containerId, playerInventory, pos);

		this.capacity = this.blockEntity.getTank().getCapacity();

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return BuiltInRegistries.FLUID.getId(ContainerRancher.this.blockEntity.getTank().getFluid().getFluid());
			}

			@Override
			public void set(int value) {
				fluidStackId = value;
			}
		});

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerRancher.this.blockEntity.getTank().getFluid().getAmount();
			}

			@Override
			public void set(int value) {
				fluidAmount = value;
			}
		});

		addSlotRange(this.blockEntity, 0, 44, 47, BlockRancherBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}
}
