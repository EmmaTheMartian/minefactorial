package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.BlockBreakerBE;
import martian.minefactorial.content.menu.ContainerBreaker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenBreaker extends AbstractMachineScreen<BlockBreakerBE, ContainerBreaker> {
	public static final ResourceLocation UI = id("textures/gui/generic_5_slot_machine.png");

	public ScreenBreaker(ContainerBreaker menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
