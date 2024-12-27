package martian.minefactorial.client.overlay.scrollmenu;

import martian.minefactorial.client.MFKeys;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class OverlayScrollMenu implements LayeredDraw.Layer {
	private static final Stack<ScrollMenu> MENUS = new Stack<>();

	public static final int LINE_HEIGHT = 20;
	public static final float EASING = 0.1f;

	@Override
	public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta) {
		if (MENUS.isEmpty()) {
			return;
		}

		// Render each menu
		for (int i = 0; i < MENUS.size(); i++) {
			int finalI = i; // Copy `i`
			MENUS.forEach(menu -> menu.render(graphics, finalI * 64));
		}

		// Get the top menu to control it
		ScrollMenu top = MENUS.peek();

		if (MFKeys.SCROLL_MENU_DOWN.get().consumeClick()) {
			top.selection++;
			if (top.selection >= top.entries.size()) {
				top.selection = 0;
			}
		} else if (MFKeys.SCROLL_MENU_UP.get().consumeClick()) {
			top.selection--;
			if (top.selection < 0) {
				top.selection = top.entries.size() - 1;
			}
		}

		if (MFKeys.SCROLL_MENU_SELECT.get().consumeClick()) {
			top.entries.get(top.selection).callback().onSelect();
			MENUS.pop();
		}
	}

	public static boolean isMenuOpen() {
		return !MENUS.isEmpty();
	}

	public static void clearMenus() {
		MENUS.clear();
	}

	public static void pushMenu(ScrollMenu menu) {
		MENUS.push(menu);
	}

	public static ScrollMenu popMenu() {
		return MENUS.pop();
	}

	public static ScrollMenu getTopMenu() {
		return MENUS.peek();
	}

	public static Stack<ScrollMenu> getMenus() {
		return MENUS;
	}
}
