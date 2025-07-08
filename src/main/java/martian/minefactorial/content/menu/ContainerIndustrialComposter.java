package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.machinery.farming.BlockIndustrialComposterBE;
import martian.minefactorial.content.block.power.BlockSteamBoilerBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.api.menu.AbstractEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerIndustrialComposter extends AbstractEnergyContainer<BlockIndustrialComposterBE> {
	public final int sewageCapacity, industrialFertilizerCapacity;
	protected int sewageMillibuckets, industrialFertilizerMillibuckets;

	public ContainerIndustrialComposter(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.INDUSTRIAL_COMPOSTER.get(), MFBlocks.INDUSTRIAL_COMPOSTER.get(), 0, containerId, playerInventory, pos);

		this.sewageCapacity = this.blockEntity.getSewageTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerIndustrialComposter.this.blockEntity.getSewageTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerIndustrialComposter.this.sewageMillibuckets = amount;
			}
		});

		this.industrialFertilizerCapacity = this.blockEntity.getIndustrialFertilizerTank().getCapacity();
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerIndustrialComposter.this.blockEntity.getIndustrialFertilizerTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerIndustrialComposter.this.industrialFertilizerMillibuckets = amount;
			}
		});

		addEnergySlot(this.blockEntity);

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getSewageMillibuckets() {
		return sewageMillibuckets;
	}

	public int getIndustrialFertilizerMillibuckets() {
		return industrialFertilizerMillibuckets;
	}
}
