package martian.minefactorial.content.menu;

import martian.minefactorial.content.block.BlockSteamTurbineBE;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerSteamTurbine extends AbstractMachineContainer<BlockSteamTurbineBE> {
	public final int steamCapacity;
	protected int steamMillibuckets;

	public ContainerSteamTurbine(int containerId, Inventory playerInventory, BlockPos pos) {
		super(MFMenuTypes.STEAM_TURBINE.get(), MFBlocks.STEAM_TURBINE.get(), 0, containerId, playerInventory, pos);

		addEnergySlot(this.blockEntity);

		this.steamCapacity = this.blockEntity.getTank().getCapacity();

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return ContainerSteamTurbine.this.blockEntity.getTank().getFluidAmount();
			}

			@Override
			public void set(int amount) {
				ContainerSteamTurbine.this.steamMillibuckets = amount;
			}
		});

		addPlayerInventorySlots(playerInventory, 8, 84);
	}

	public int getSteamMillibuckets() {
		return steamMillibuckets;
	}
}
