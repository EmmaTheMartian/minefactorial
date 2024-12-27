package martian.minefactorial.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public final class MFKeys {
	private MFKeys() { }

	public static final Lazy<KeyMapping> SHOW_EXTENDED_TOOLTIP = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.show_extended_tooltip",
			KeyConflictContext.GUI,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT_SHIFT,
			"key.categories.minefactorial"
	));

	public static final Lazy<KeyMapping> SCROLL_MENU_UP = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.scroll_menu_up",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_PAGE_UP,
			"key.categories.minefactorial"
	));

	public static final Lazy<KeyMapping> SCROLL_MENU_DOWN = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.scroll_menu_down",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_PAGE_DOWN,
			"key.categories.minefactorial"
	));

	public static final Lazy<KeyMapping> SCROLL_MENU_SELECT = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.scroll_menu_select",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_ENTER,
			"key.categories.minefactorial"
	));

	public static final Lazy<KeyMapping> TWEAKERULER_UNDO = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.tweakeruler_undo",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_Z,
			"key.categories.minefactorial"
	));

	public static final Lazy<KeyMapping> TWEAKERULER_REDO = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.tweakeruler_redo",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_Y,
			"key.categories.minefactorial"
	));
}
