package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.husbandry.BlockSlaughterhouseBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerSlaughterhouse extends AbstractMachineContainer<BlockSlaughterhouseBE> {
	public final int pinkSlimeCapacity;
	public final int meatCapacity;
	protected int pinkSlimeMillibuckets;
	protected int meatMillibuckets;

	public ContainerSlaughterhouse(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.SLAUGHTERHOUSE.get(), MFBlocks.SLAUGHTERHOUSE.get(), BlockSlaughterhouseBE.SLOTS, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);
		addWorkSlot(this.blockEntity);
		addIdleSlot(this.blockEntity);

		this.pinkSlimeCapacity = this.blockEntity.getPinkSlimeTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSlaughterhouse.this.blockEntity.getPinkSlimeTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerSlaughterhouse.this.pinkSlimeMillibuckets = amount;
			}
		});

		this.meatCapacity = this.blockEntity.getMeatTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSlaughterhouse.this.blockEntity.getMeatTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerSlaughterhouse.this.meatMillibuckets = amount;
			}
		});

		addSlotRange(this.blockEntity, 0, 18, 47, BlockSlaughterhouseBE.SLOTS, 18);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getPinkSlimeMillibuckets() {
		return pinkSlimeMillibuckets;
	}

	public int getMeatMillibuckets() {
		return meatMillibuckets;
	}
}
