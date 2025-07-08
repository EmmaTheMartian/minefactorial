package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.farming.BlockPlanterBE;
import martian.minefactorial.content.menu.ContainerPlanter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenPlanter extends AbstractMachineScreen<BlockPlanterBE, ContainerPlanter> {
	public static final ResourceLocation UI = id("textures/gui/rainbow_9_slot_machine.png");

	public ScreenPlanter(ContainerPlanter menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageHeight = 186;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
