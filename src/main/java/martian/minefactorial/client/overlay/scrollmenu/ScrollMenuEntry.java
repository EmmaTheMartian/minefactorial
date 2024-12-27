package martian.minefactorial.client.overlay.scrollmenu;

import net.minecraft.network.chat.Component;

public record ScrollMenuEntry(Component text, Callback callback) {
	@FunctionalInterface
	public interface Callback {
		void onSelect();
	}
}
