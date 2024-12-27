package martian.minefactorial.client.overlay.scrollmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ScrollMenu {
	public List<ScrollMenuEntry> entries = new ArrayList<>();
	public int selection = 0;

	public int x = 8;
	public int y = 0;
	public int targetY = y;

	public int textColourUnselected = CommonColors.WHITE;
	public int textColourSelected = CommonColors.YELLOW;

	public ScrollMenu() {
	}

	public ScrollMenu addEntry(ScrollMenuEntry entry) {
		entries.add(entry);
		return this;
	}

	public void render(GuiGraphics graphics, int xOffset) {
		targetY = ((entries.size() - selection) * OverlayScrollMenu.LINE_HEIGHT);
		y = Mth.lerpInt(OverlayScrollMenu.EASING, y, targetY);

		ScrollMenuEntry entry;
		for (int i = 0; i < entries.size(); i++) {
			entry = entries.get(i);
			graphics.drawString(
					Minecraft.getInstance().font,
					entry.text(),
					xOffset + (i == selection ? x + 4 : x),
					y + (i * OverlayScrollMenu.LINE_HEIGHT),
					i == selection ? textColourSelected : textColourUnselected
			);
		}
	}

	public void triggerSelect(boolean shouldCloseMenu) {
		entries.get(selection).callback().onSelect();
		if (shouldCloseMenu) {
			OverlayScrollMenu.popMenu();
		}
	}
}
