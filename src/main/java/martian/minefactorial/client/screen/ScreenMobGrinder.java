package martian.minefactorial.client.screen;

import martian.minefactorial.content.block.machinery.BlockMobGrinderBE;
import martian.minefactorial.content.menu.ContainerMobGrinder;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.client.FluidRenderer;
import martian.minefactorial.foundation.client.screen.AbstractMachineScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public class ScreenMobGrinder extends AbstractMachineScreen<BlockMobGrinderBE, ContainerMobGrinder> {
	public static final ResourceLocation UI = id("textures/gui/mob_grinder.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			TANK_X = 143, TANK_Y = 22;

	public ScreenMobGrinder(ContainerMobGrinder menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		int mb = this.menu.getEssenceMillibuckets();
		if (mb > 0) {
			float filled_percent = ((float) mb / this.menu.essenceCapacity);
			int height = (int) (TANK_HEIGHT * filled_percent);

			FluidRenderer.renderFluidGui(
					new FluidStack(MFFluids.ESSENCE, mb),
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

		int mb = this.menu.getEssenceMillibuckets();
		if (mb > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + TANK_X, topPos + TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Essence: " + mb + "mB"), mouseX, mouseY);
		}
	}
}
