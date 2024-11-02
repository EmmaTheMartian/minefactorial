package martian.minefactorial.foundation.pipenet;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PipeState implements StringRepresentable {
	DISCONNECTED,
	CONNECTED,
	BLOCK,
	;

	@Override
	public @NotNull String getSerializedName() {
		return this.name().toLowerCase();
	}
}
