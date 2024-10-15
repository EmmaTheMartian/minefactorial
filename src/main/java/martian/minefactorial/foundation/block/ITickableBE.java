package martian.minefactorial.foundation.block;

public interface ITickableBE {
	default void serverTick() { }

	default void clientTick() { }
}
