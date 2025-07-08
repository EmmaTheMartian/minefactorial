package martian.minefactorial.integration.emi;

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import martian.minefactorial.content.recipe.RecipeMeatPacking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import top.girlkisser.lazuli.api.block.IEnergyBE;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

public class EmiRecipeMeatPacking extends BasicEmiRecipe {
	private static final int PADDING_Y = 8;
	private static final int PADDING_X = 8;

	private final int powerPerTick, craftDuration;
	private final int totalPowerCost;

	public EmiRecipeMeatPacking(RecipeHolder<RecipeMeatPacking> recipe) {
		super(MFEmiPlugin.MEAT_PACKING, recipe.id(), (PADDING_X * 2) + 105, (PADDING_Y * 2) + 44);
		this.id = recipe.id();
		this.powerPerTick = recipe.value().powerPerTick();
		this.craftDuration = recipe.value().craftDuration();
		this.inputs.add(NeoForgeEmiIngredient.of(recipe.value().input()));
		this.outputs.add(EmiStack.of(recipe.value().result()));
		totalPowerCost = powerPerTick * craftDuration;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, PADDING_X + 32, PADDING_Y + 24);
		widgets.addTexture(MFEmiPlugin.ENERGY_BAR, PADDING_X, PADDING_Y);
		widgets.addTexture(MFEmiPlugin.WORK_BAR, PADDING_X, PADDING_Y + 6);

		// Input
		widgets.addSlot(this.inputs.getFirst(), PADDING_X + 7, PADDING_Y + 23);

		// Result
		widgets.addSlot(this.outputs.getFirst(), PADDING_X + 61, PADDING_Y + 23);

		// Render energy cost
		widgets.addDrawable(PADDING_X, PADDING_Y, 105, 4, (GuiGraphics graphics, int mouseX, int mouseY, float partialTick) -> {
			graphics.fill(6, 1, 6 + ((totalPowerCost / IEnergyBE.DEFAULT_MAX_ENERGY) * 99), 3, 0xFFEBE350);
			// Tooltip
			if (Mathematics.pointWithinRectangle(mouseX, mouseY, PADDING_X, PADDING_Y, 105, 4)) {
				graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(totalPowerCost + " Energy (" + powerPerTick + "/t)"), mouseX, mouseY);
			}
		});

		// Render craft duration
		widgets.addDrawable(PADDING_X, PADDING_Y + 6, 105, 4, (GuiGraphics graphics, int mouseX, int mouseY, float partialTick) -> {
			assert Minecraft.getInstance().level != null;
			float tick = Minecraft.getInstance().level.getGameTime() % craftDuration;
			float durationPercent = tick / craftDuration;

			graphics.fill(6, 1, 6 + (int) (durationPercent * 99), 3, 0xFF3CD05B);

			// Tooltip
			if (Mathematics.pointWithinRectangle(mouseX, mouseY, PADDING_X, PADDING_Y + 6, 105, 4)) {
				graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(craftDuration + " Ticks"), mouseX, mouseY);
			}
		});
	}
}
