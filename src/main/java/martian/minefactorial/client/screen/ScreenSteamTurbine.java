package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.power.BlockSteamTurbineBE;
import martian.minefactorial.content.menu.ContainerSteamTurbine;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.client.FluidRenderer;
import martian.minefactorial.foundation.client.screen.AbstractEnergyScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public class ScreenSteamTurbine extends AbstractEnergyScreen<BlockSteamTurbineBE, ContainerSteamTurbine> {
	public static final ResourceLocation UI = id("textures/gui/steam_turbine.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			STEAM_TANK_X = 80, STEAM_TANK_Y = 29;

	public ScreenSteamTurbine(ContainerSteamTurbine menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		int steam = this.menu.getSteamMillibuckets();

		if (steam > 0) {
			FluidRenderer.renderFluidGui(
					new FluidStack(MFFluids.STEAM, steam),
					graphics,
					leftPos + STEAM_TANK_X,
					topPos + STEAM_TANK_Y,
					TANK_WIDTH,
					(int) (TANK_HEIGHT * ((float) steam / this.menu.steamCapacity))
			);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		int steam = this.menu.getSteamMillibuckets();

		if (steam > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + STEAM_TANK_X, topPos + STEAM_TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Steam: " + steam + "mB"), mouseX, mouseY);
		}
	}
}
