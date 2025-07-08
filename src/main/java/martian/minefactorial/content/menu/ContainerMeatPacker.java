package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.BlockMeatPackerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import top.girlkisser.lazuli.api.menu.SlotOutputOnly;

public class ContainerMeatPacker extends AbstractMachineContainer<BlockMeatPackerBE> {
	public final int capacity;
	public int fluidStackId, fluidAmount;

	public ContainerMeatPacker(int containerId, Inventory playerInventory, BlockPos pos) {
		super(
				MFMenuTypes.MEAT_PACKER.get(),
				MFBlocks.MEAT_PACKER.get(),
				BlockMeatPackerBE.SLOTS,
				containerId,
				playerInventory,
				pos
		);

		this.capacity = this.blockEntity.getTank().getCapacity();

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return BuiltInRegistries.FLUID.getId(ContainerMeatPacker.this.blockEntity.getTank().getFluid().getFluid());
			}

			@Override
			public void set(int value) {
				fluidStackId = value;
			}
		});

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerMeatPacker.this.blockEntity.getTank().getFluid().getAmount();
			}

			@Override
			public void set(int value) {
				fluidAmount = value;
			}
		});

		addSlot(new SlotOutputOnly(this.blockEntity, 0, 107, 50));

		addPlayerInventorySlots(playerInventory, 8, 94);
	}
}
