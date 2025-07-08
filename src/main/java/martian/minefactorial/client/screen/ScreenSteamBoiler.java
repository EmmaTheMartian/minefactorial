package martian.minefactorial.client.screen;

import martian.minefactorial.content.menu.ContainerSteamBoiler;
import martian.minefactorial.content.registry.MFFluids;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.client.FluidRenderingHelpers;
import top.girlkisser.lazuli.api.client.screen.AbstractLazuliContainerScreen;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

import static martian.minefactorial.Minefactorial.id;

public class ScreenSteamBoiler extends AbstractLazuliContainerScreen<ContainerSteamBoiler> {
	public static final ResourceLocation UI = id("textures/gui/steam_boiler.png");
	public static final ResourceLocation BURN_PROGRESS_SPRITE = id("minecraft", "container/furnace/lit_progress");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			WATER_TANK_X = 71, WATER_TANK_Y = 28,
			STEAM_TANK_X = 125, STEAM_TANK_Y = 28,
			BURN_ICON_X = 35, BURN_ICON_Y = 36;

	public ScreenSteamBoiler(ContainerSteamBoiler menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		int water = this.menu.getWaterMillibuckets();
		int steam = this.menu.getSteamMillibuckets();
		int burnTicksLeft = this.menu.getBurnTicksLeft();

		if (water >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(Fluids.WATER, water),
					menu.waterCapacity,
					graphics,
					leftPos + WATER_TANK_X,
					topPos + WATER_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}

		if (steam >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.STEAM, steam),
					menu.steamCapacity,
					graphics,
					leftPos + STEAM_TANK_X,
					topPos + STEAM_TANK_Y,
					TANK_WIDTH,
					TANK_HEIGHT
			);
		}

		if (burnTicksLeft > 0) {
			int spriteHeight = Mth.ceil((float) this.menu.getBurnTicksLeft() / this.menu.getTotalBurnTicksForFuel() * 13.0F) + 1;
			graphics.blitSprite(BURN_PROGRESS_SPRITE, 14, 14, 0, 14 - spriteHeight, this.leftPos + BURN_ICON_X, this.topPos + BURN_ICON_Y + 14 - spriteHeight, 14, spriteHeight);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		int water = this.menu.getWaterMillibuckets();
		int steam = this.menu.getSteamMillibuckets();
		int burnTicksLeft = this.menu.getBurnTicksLeft();

		if (water > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + WATER_TANK_X, topPos + WATER_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Water: " + water + "mB"), mouseX, mouseY);
		} else if (steam > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + STEAM_TANK_X, topPos + STEAM_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Steam: " + steam + "mB"), mouseX, mouseY);
		} else if (burnTicksLeft > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + BURN_ICON_X, topPos + BURN_ICON_Y, 14, 14)) {
			graphics.renderTooltip(this.font, Component.literal("Burn Ticks: " + burnTicksLeft + "/" + menu.getTotalBurnTicksForFuel()), mouseX, mouseY);
		}
	}
}
