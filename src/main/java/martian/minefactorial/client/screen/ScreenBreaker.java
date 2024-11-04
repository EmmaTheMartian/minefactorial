package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.machinery.BlockBreakerBE;
import martian.minefactorial.content.menu.ContainerBreaker;
import martian.minefactorial.foundation.client.screen.AbstractMachineScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenBreaker extends AbstractMachineScreen<BlockBreakerBE, ContainerBreaker> {
	public ScreenBreaker(ContainerBreaker menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}
}
