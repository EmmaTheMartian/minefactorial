package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.machinery.BlockPlacerBE;
import martian.minefactorial.content.menu.ContainerPlacer;
import martian.minefactorial.foundation.client.screen.AbstractMachineScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenPlacer extends AbstractMachineScreen<BlockPlacerBE, ContainerPlacer> {
	public static final ResourceLocation UI = id("textures/gui/generic_1_slot_machine.png");

	public ScreenPlacer(ContainerPlacer menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
