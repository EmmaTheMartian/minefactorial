package martian.minefactorial.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public final class MFKeys {
	private MFKeys() { }

	public static final Lazy<KeyMapping> SHOW_EXTENDED_TOOLTIP = Lazy.of(() -> new KeyMapping(
			"key.minefactorial.show_extended_tooltip",
			KeyConflictContext.GUI,
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT_SHIFT,
			"key.categories.misc"
	));
}
