package martian.minefactorial.foundation.client.screen;

import martian.minefactorial.foundation.menu.AbstractMFContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMFScreen<T extends AbstractMFContainer> extends AbstractContainerScreen<T> {
	protected AbstractMFScreen(T menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	protected abstract ResourceLocation getUI();

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		graphics.blit(getUI(), leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}
}
