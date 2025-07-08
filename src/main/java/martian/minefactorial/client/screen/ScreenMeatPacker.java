package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.BlockMeatPackerBE;
import martian.minefactorial.content.menu.ContainerMeatPacker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.client.FluidRenderingHelpers;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

import static martian.minefactorial.Minefactorial.id;

public class ScreenMeatPacker extends AbstractMachineScreen<BlockMeatPackerBE, ContainerMeatPacker> {
	public static final ResourceLocation UI = id("textures/gui/generic_fluid_in_item_out_machine.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			TANK_X = 53, TANK_Y = 38;

	public ScreenMeatPacker(ContainerMeatPacker menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageHeight = 175;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		if (this.menu.fluidAmount >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(BuiltInRegistries.FLUID.byId(this.menu.fluidStackId), this.menu.fluidAmount),
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

		if (this.menu.fluidAmount > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + TANK_X, topPos + TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			FluidStack fluidStack = new FluidStack(BuiltInRegistries.FLUID.byId(this.menu.fluidStackId), this.menu.fluidAmount);
			graphics.renderTooltip(this.font, fluidStack.getHoverName().copy().append(": " + this.menu.fluidAmount + "mB"), mouseX, mouseY);
		}
	}
}
