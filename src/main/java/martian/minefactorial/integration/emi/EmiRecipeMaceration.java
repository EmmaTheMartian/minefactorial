package martian.minefactorial.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import martian.minefactorial.content.recipe.RecipeMaceration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.lazuli.api.mathematics.Mathematics;

import java.util.List;

public class EmiRecipeMaceration implements EmiRecipe {
	private static final int PADDING_Y = 8;
	private static final int PADDING_X = 8;

	private final ResourceLocation id;
	private final int powerPerTick, craftDuration;
	private final List<EmiIngredient> input;
	private final List<EmiStack> results;

	private final int totalPowerCost;

	public EmiRecipeMaceration(RecipeHolder<RecipeMaceration> recipe) {
		this.id = recipe.id();
		this.powerPerTick = recipe.value().powerPerTick();
		this.craftDuration = recipe.value().craftDuration();
		this.input = List.of(EmiIngredient.of(recipe.value().input()));
		this.results = recipe.value()
				.results()
				.stream()
				.map(it -> EmiStack.of(it.stack()).setChance(it.chance()))
				.toList();

		totalPowerCost = powerPerTick * craftDuration;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return MFEmiPlugin.MACERATION;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return input;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return results;
	}

	@Override
	public int getDisplayWidth() {
		return (PADDING_X * 2) + 105;
	}

	@Override
	public int getDisplayHeight() {
		return (PADDING_Y * 2) + 44;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, PADDING_X + 32, PADDING_Y + 24);
		widgets.addTexture(MFEmiPlugin.ENERGY_BAR, PADDING_X, PADDING_Y);
		widgets.addTexture(MFEmiPlugin.WORK_BAR, PADDING_X, PADDING_Y + 6);

		// Input
		widgets.addSlot(input.getFirst(), PADDING_X + 7, PADDING_Y + 23);

		// Results
		final int startX = PADDING_X + 61;
		final int startY = PADDING_Y + 14;
		final int dx = 18;
		final int dy = 18;
		final int width = 2;
		final int height = 2;
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				widgets.addSlot(
						index >= results.size() ?
								EmiStack.EMPTY :
								results.get(index),
						startX + (dx * x),
						startY + (dy * y)
				);
				index++;
			}
		}

		// Render energy cost
		widgets.addDrawable(PADDING_X, PADDING_Y, 105, 4, (GuiGraphics graphics, int mouseX, int mouseY, float partialTick) -> {
			// 1600 is the amount of energy in a macerator
			graphics.fill(6, 1, 6 + (int) ((totalPowerCost / 1600f) * 99), 3, 0xFFEBE350);
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
