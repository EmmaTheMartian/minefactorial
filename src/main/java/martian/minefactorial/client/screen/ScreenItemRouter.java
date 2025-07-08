package martian.minefactorial.client.screen;

import martian.minefactorial.content.menu.ContainerItemRouter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.girlkisser.lazuli.api.client.screen.AbstractLazuliContainerScreen;

import static martian.minefactorial.Minefactorial.id;

public class ScreenItemRouter extends AbstractLazuliContainerScreen<ContainerItemRouter> {
	public static final ResourceLocation UI = id("textures/gui/generic_9x6_rainbow.png");

	public ScreenItemRouter(ContainerItemRouter menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageHeight = 221;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}
}
