package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.BlockMaceratorBE;
import martian.minefactorial.content.menu.ContainerMacerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenMacerator extends AbstractMachineScreen<BlockMaceratorBE, ContainerMacerator> {
	public static final ResourceLocation UI = id("textures/gui/generic_1_in_4_out_machine.png");

	public ScreenMacerator(ContainerMacerator menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
