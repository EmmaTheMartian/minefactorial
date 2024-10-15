package martian.minefactorial.foundation.client.screen;

import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.block.AbstractEnergyBE;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEnergyScreen<T extends AbstractEnergyBE, U extends AbstractMachineContainer<T>>
		extends AbstractMFScreen<U>
{
	public static final int
			BAR_WIDTH = 100, BAR_HEIGHT = 4,
			ENERGY_BAR_X = 12, ENERGY_BAR_Y = 15;

	protected AbstractEnergyScreen(U menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		assert this.menu.blockEntity != null;

		float power_percent = (float) this.menu.getPower() / (float) this.menu.blockEntity.getMaxEnergy();
		if (power_percent > 0) {
			graphics.fill(
					leftPos + ENERGY_BAR_X + 1,
					topPos + ENERGY_BAR_Y + 1,
					leftPos + ENERGY_BAR_X + (int) (power_percent * BAR_WIDTH) - 1,
					topPos + ENERGY_BAR_Y + BAR_HEIGHT - 1,
					0xFFEBE350
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		// Render energy tooltip
		assert this.menu.blockEntity != null;
//		if (hasEnergy && mouseX >= leftPos + ENERGY_BAR_X && mouseX < leftPos + ENERGY_BAR_X + BAR_WIDTH && mouseY >= topPos + ENERGY_BAR_Y && mouseY < topPos + ENERGY_BAR_Y + BAR_HEIGHT) {
		if (Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + ENERGY_BAR_X, topPos + ENERGY_BAR_Y, BAR_WIDTH, BAR_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal(this.menu.getPower() + "/" + this.menu.blockEntity.getMaxEnergy() + " FE"), mouseX, mouseY);
		}
	}
}
