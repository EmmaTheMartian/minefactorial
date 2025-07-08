package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractEnergyScreen;
import martian.minefactorial.content.block.machinery.farming.BlockIndustrialComposterBE;
import martian.minefactorial.content.menu.ContainerIndustrialComposter;
import martian.minefactorial.content.registry.MFFluids;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.client.FluidRenderingHelpers;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

import static martian.minefactorial.Minefactorial.id;

public class ScreenIndustrialComposter extends AbstractEnergyScreen<BlockIndustrialComposterBE, ContainerIndustrialComposter> {
	public static final ResourceLocation UI = id("textures/gui/industrial_composter.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			SEWAGE_TANK_X = 53, SEWAGE_TANK_Y = 28,
			FERTILIZER_TANK_X = 107, FERTILIZER_TANK_Y = 28;

	public ScreenIndustrialComposter(ContainerIndustrialComposter menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		if (menu.getSewageMillibuckets() >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.SEWAGE, menu.getSewageMillibuckets()),
					menu.sewageCapacity,
					graphics,
					leftPos + SEWAGE_TANK_X,
					topPos + SEWAGE_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}

		if (menu.getIndustrialFertilizerMillibuckets() >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.INDUSTRIAL_FERTILIZER, menu.getIndustrialFertilizerMillibuckets()),
					menu.industrialFertilizerCapacity,
					graphics,
					leftPos + FERTILIZER_TANK_X,
					topPos + FERTILIZER_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		int sewage = this.menu.getSewageMillibuckets();
		int fertilizer = this.menu.getIndustrialFertilizerMillibuckets();

		if (sewage > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + SEWAGE_TANK_X, topPos + SEWAGE_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Sewage: " + sewage + "mB"), mouseX, mouseY); //todo: translatable
		} else if (fertilizer > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + FERTILIZER_TANK_X, topPos + FERTILIZER_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Fertilizer: " + fertilizer + "mB"), mouseX, mouseY); //todo: translatable
		}
	}
}
