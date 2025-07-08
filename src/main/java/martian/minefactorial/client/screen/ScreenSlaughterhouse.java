package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.husbandry.BlockSlaughterhouseBE;
import martian.minefactorial.content.menu.ContainerSlaughterhouse;
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

public class ScreenSlaughterhouse extends AbstractMachineScreen<BlockSlaughterhouseBE, ContainerSlaughterhouse> {
	public static final ResourceLocation UI = id("textures/gui/generic_5_slot_2_tank_machine.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			PINK_SLIME_TANK_X = 144, PINK_SLIME_TANK_Y = 22,
			MEAT_TANK_X = 124, MEAT_TANK_Y = 22;

	public ScreenSlaughterhouse(ContainerSlaughterhouse menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		if (this.menu.getPinkSlimeMillibuckets() >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.PINK_SLIME, this.menu.getPinkSlimeMillibuckets()),
					menu.pinkSlimeCapacity,
					graphics,
					leftPos + PINK_SLIME_TANK_X,
					topPos + PINK_SLIME_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}

		if (this.menu.getMeatMillibuckets() >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.MEAT, this.menu.getMeatMillibuckets()),
					menu.meatCapacity,
					graphics,
					leftPos + MEAT_TANK_X,
					topPos + MEAT_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		int pinkSlimeMb = this.menu.getPinkSlimeMillibuckets();
		if (pinkSlimeMb > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + PINK_SLIME_TANK_X, topPos + PINK_SLIME_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Pink Slime: " + pinkSlimeMb + "mB"), mouseX, mouseY);
		}

		int meatMb = this.menu.getMeatMillibuckets();
		if (meatMb > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + MEAT_TANK_X, topPos + MEAT_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Meat: " + meatMb + "mB"), mouseX, mouseY);
		}
	}
}
