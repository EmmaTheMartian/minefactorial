package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractEnergyScreen;
import martian.minefactorial.content.block.storage.BlockCapacitorBE;
import martian.minefactorial.content.menu.ContainerCapacitor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static martian.minefactorial.Minefactorial.id;

public class ScreenCapacitor extends AbstractEnergyScreen<BlockCapacitorBE, ContainerCapacitor> {
	public static final ResourceLocation UI = id("textures/gui/capacitor.png");

	public ScreenCapacitor(ContainerCapacitor menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
