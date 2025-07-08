package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.BlockSmasherBE;
import martian.minefactorial.content.menu.ContainerSmasher;
import martian.minefactorial.content.net.PacketServerboundSetSmasherFortuneLevel;
import martian.minefactorial.content.registry.MFFluids;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.client.FluidRenderingHelpers;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

import javax.annotation.ParametersAreNonnullByDefault;

import static martian.minefactorial.Minefactorial.id;

public class ScreenSmasher extends AbstractMachineScreen<BlockSmasherBE, ContainerSmasher> {
	public static final ResourceLocation UI = id("textures/gui/smasher.png");

	public static final int
			TANK_WIDTH = 16, TANK_HEIGHT = 41,
			TANK_X = 143, TANK_Y = 22,
			UP_ARROW_X = 10, UP_ARROW_Y = 39,
			DOWN_ARROW_X = 10, DOWN_ARROW_Y = 64;

	private UpArrowButton upArrowButton;
	private DownArrowButton downArrowButton;

	private int fortuneLevel;

	public ScreenSmasher(ContainerSmasher menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void init() {
		super.init();

		upArrowButton = new UpArrowButton(this.leftPos + UP_ARROW_X, this.topPos + UP_ARROW_Y);
		downArrowButton = new DownArrowButton(this.leftPos + DOWN_ARROW_X, this.topPos + DOWN_ARROW_Y);

		addRenderableWidget(upArrowButton);
		addRenderableWidget(downArrowButton);

		fortuneLevel = menu.getFortuneLevel();

		menu.addSlotListener(new ContainerListener() {
			@Override
			@ParametersAreNonnullByDefault
			public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
			}

			@Override
			@ParametersAreNonnullByDefault
			public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
				ScreenSmasher.this.fortuneLevel = menu.getFortuneLevel();
			}
		});
	}

	@Override
	protected ResourceLocation getUI() {
		return UI;
	}

	@Override
	public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		super.renderBg(graphics, partialTick, mouseX, mouseY);

		// Render fortune level
		graphics.drawCenteredString(font, Integer.toString(fortuneLevel), this.leftPos + 16, this.topPos + 51, 0xFFFFFFFF);

		// Render buttons
		upArrowButton.render(graphics, mouseX, mouseY, partialTick);
		downArrowButton.render(graphics, mouseX, mouseY, partialTick);

		// Render fluid
		if (this.menu.fluidAmount >= 0) {
			FluidRenderingHelpers.renderFluidTankGui(
					new FluidStack(MFFluids.ESSENCE, this.menu.fluidAmount),
					menu.tankCapacity,
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

		int mb = this.menu.getFluidAmount();
		if (mb > 0 && Mathematics.pointWithinRectangle(mouseX, mouseY, leftPos + TANK_X, topPos + TANK_Y, TANK_WIDTH, TANK_HEIGHT)) {
			graphics.renderTooltip(this.font, Component.literal("Essence: " + mb + "mB"), mouseX, mouseY);
		}
	}

	@Override
	public void containerTick() {
		super.containerTick();
		// Update buttons
		int level = this.menu.getFortuneLevel();
		upArrowButton.updateStatus(level);
		downArrowButton.updateStatus(level);
	}

	private class UpArrowButton extends ImageButton {
		private static final WidgetSprites sprites = new WidgetSprites(
			id("arrows/up"),
			id("arrows/up"),
			id("arrows/up_highlighted"),
			id("arrows/up")
		);

		public UpArrowButton(int x, int y) {
			super(x, y, 12, 7, sprites, button -> ScreenSmasher.this.fortuneLevel++, Component.literal("Increment Fortune Level"));
		}

		public void updateStatus(int level) {
			this.active = level < BlockSmasherBE.MAX_FORTUNE_LEVEL;
		}

		@Override
		public void onPress() {
			super.onPress();
			PacketDistributor.sendToServer(new PacketServerboundSetSmasherFortuneLevel(fortuneLevel));
		}
	}

	private class DownArrowButton extends ImageButton {
		private static final WidgetSprites sprites = new WidgetSprites(
				id("arrows/down"),
				id("arrows/down"),
				id("arrows/down_highlighted"),
				id("arrows/down")
		);

		public DownArrowButton(int x, int y) {
			super(x, y, 12, 7, sprites, button -> ScreenSmasher.this.fortuneLevel--, Component.literal("Decrement Fortune Level"));
		}

		public void updateStatus(int level) {
			this.active = level >= 0;
		}

		@Override
		public void onPress() {
			super.onPress();
			PacketDistributor.sendToServer(new PacketServerboundSetSmasherFortuneLevel(fortuneLevel));
		}
	}
}
