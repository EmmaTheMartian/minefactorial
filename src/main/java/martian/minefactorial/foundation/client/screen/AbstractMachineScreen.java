package martian.minefactorial.foundation.client.screen;

import martian.minefactorial.foundation.block.AbstractMachineBE;
import martian.minefactorial.foundation.menu.AbstractMachineContainer;
import martian.minefactorial.foundation.Mathematics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public abstract class AbstractMachineScreen<T extends AbstractMachineBE, U extends AbstractMachineContainer<T>>
		extends AbstractEnergyScreen<T, U>
{
	public static final int
			WORK_BAR_X = 12, WORK_BAR_Y = 21,
			IDLE_BAR_X = 12, IDLE_BAR_Y = 27;

	public static final ResourceLocation BLANK_MACHINE_UI = id("textures/gui/blank_machine.png");

	protected boolean hasWork = true, hasIdle = true;

	protected AbstractMachineScreen(U menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	protected ResourceLocation getUI() {
		return BLANK_MACHINE_UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		assert this.menu.blockEntity != null;

		float work_percent = (float) this.menu.getWork() / (float) this.menu.blockEntity.getMaxWork();
		if (work_percent > 0) {
			graphics.fill(
					leftPos + WORK_BAR_X + 1,
					topPos + WORK_BAR_Y + 1,
					leftPos + WORK_BAR_X + (int) (work_percent * BAR_WIDTH) - 1,
					topPos + WORK_BAR_Y + BAR_HEIGHT - 1,
					0xFF3CD05B
			);
		}

		float idle_percent = (float) this.menu.getIdle() / (float) this.menu.blockEntity.getIdleTime();
		if (idle_percent > 0) {
			graphics.fill(
					leftPos + IDLE_BAR_X + 1,
					topPos + IDLE_BAR_Y + 1,
					leftPos + IDLE_BAR_X + (int) (idle_percent * BAR_WIDTH) - 1,
					topPos + IDLE_BAR_Y + BAR_HEIGHT - 1,
					0xFF3C7ED0
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		// Render work and idle tooltips
		assert this.menu.blockEntity != null;

		if (hasWork && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + WORK_BAR_X, topPos + WORK_BAR_Y, BAR_WIDTH, BAR_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal(this.menu.getWork() + "/" + this.menu.blockEntity.getMaxWork() + " Work"), mouseX, mouseY);
		} else if (hasIdle && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + IDLE_BAR_X, topPos + IDLE_BAR_Y, BAR_WIDTH, BAR_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal(this.menu.getIdle() + "/" + this.menu.blockEntity.getIdleTime() + " Idle"), mouseX, mouseY);
		}
	}
}
