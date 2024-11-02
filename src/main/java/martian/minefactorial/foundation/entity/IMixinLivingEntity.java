package martian.minefactorial.foundation.entity;

public interface IMixinLivingEntity {
	void minefactorial$setShouldSkipDrops(boolean value);

	boolean minefactorial$shouldSkipDrops();
}
