package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.machinery.husbandry.BlockSewageCollectorBE;
import martian.minefactorial.content.menu.ContainerSewageCollector;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.client.FluidRenderer;
import martian.minefactorial.foundation.client.screen.AbstractEnergyScreen;
import martian.minefactorial.foundation.client.screen.AbstractMFScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public class ScreenSewageCollector extends AbstractMFScreen<ContainerSewageCollector> {
	public static final ResourceLocation UI = id("textures/gui/sewage_collector.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			TANK_X = 80, TANK_Y = 28;

	public ScreenSewageCollector(ContainerSewageCollector menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		float cooldownPercent = this.menu.cooldownTicks / (float) BlockSewageCollectorBE.COOLDOWN_TICKS;
		if (cooldownPercent > 0) {
			graphics.fill(
					leftPos + AbstractEnergyScreen.ENERGY_BAR_X + 1,
					topPos + AbstractEnergyScreen.ENERGY_BAR_Y + 1,
					leftPos + AbstractEnergyScreen.ENERGY_BAR_X + (int) (cooldownPercent * AbstractEnergyScreen.BAR_WIDTH) - 1,
					topPos + AbstractEnergyScreen.ENERGY_BAR_Y + AbstractEnergyScreen.BAR_HEIGHT - 1,
					0xFF3CD05B
			);
		}

		if (this.menu.fluidAmount >= 0) {
			FluidRenderer.renderFluidTankGui(
					new FluidStack(MFFluids.SEWAGE, this.menu.fluidAmount),
					menu.capacity,
					graphics,
					leftPos + TANK_X,
					topPos + TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		if (Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + AbstractEnergyScreen.ENERGY_BAR_X, topPos + AbstractEnergyScreen.ENERGY_BAR_Y, AbstractEnergyScreen.BAR_WIDTH, AbstractEnergyScreen.BAR_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal(this.menu.cooldownTicks + "/" + BlockSewageCollectorBE.COOLDOWN_TICKS), mouseX, mouseY);
		}

		if (this.menu.fluidAmount > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + TANK_X, topPos + TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			FluidStack fluidStack = new FluidStack(MFFluids.SEWAGE, this.menu.fluidAmount);
			graphics.renderTooltip(this.font, fluidStack.getHoverName().copy().append(": " + this.menu.fluidAmount + "mB"), mouseX, mouseY);
		}
	}
}
