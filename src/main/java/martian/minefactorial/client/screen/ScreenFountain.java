package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.BlockFountainBE;
import martian.minefactorial.content.menu.ContainerFountain;
import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.client.FluidRenderer;
import martian.minefactorial.foundation.client.screen.AbstractMachineScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public class ScreenFountain extends AbstractMachineScreen<BlockFountainBE, ContainerFountain> {
	public static final ResourceLocation UI = id("textures/gui/fountain.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			TANK_X = 80, TANK_Y = 36;

	public ScreenFountain(ContainerFountain menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		if (this.menu.fluidAmount > 0) {
			float filled_percent = ((float) this.menu.fluidAmount / this.menu.capacity);
			int height = (int) (TANK_HEIGHT * filled_percent);

			FluidRenderer.renderFluidGui(
					new FluidStack(BuiltInRegistries.FLUID.byId(this.menu.fluidStackId), this.menu.fluidAmount),
					graphics,
					leftPos + TANK_X,
					topPos + TANK_Y + (TANK_HEIGHT - height), // we do this to render from the bottom to the top
					TANK_WIDTH,
					height
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		if (this.menu.fluidAmount > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + TANK_X, topPos + TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			FluidStack fluidStack = new FluidStack(BuiltInRegistries.FLUID.byId(this.menu.fluidStackId), this.menu.fluidAmount);
			graphics.renderTooltip(this.font, fluidStack.getHoverName().copy().append(": " + this.menu.fluidAmount + "mB"), mouseX, mouseY);
		}
	}
}
