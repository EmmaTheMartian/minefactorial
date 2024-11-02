package martian.minefactorial.foundation;

import javax.annotation.Nullable;
import java.util.function.Function;

// Lazy but with an argument
public class ArgLazy<ItT, ArgT> {
	private @Nullable ItT it;
	private final Function<ArgT, ItT> function;

	public ArgLazy(Function<ArgT, ItT> function) {
		this.function = function;
	}

	public ItT get(ArgT arg) {
		if (it == null)
			it = function.apply(arg);
		return it;
	}

	public boolean isPresent() {
		return it != null;
	}
}
