package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.farming.BlockHarvesterBE;
import martian.minefactorial.content.menu.ContainerHarvester;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenHarvester extends AbstractMachineScreen<BlockHarvesterBE, ContainerHarvester> {
	public static final ResourceLocation UI = id("textures/gui/generic_5_slot_machine.png");

	public ScreenHarvester(ContainerHarvester menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
