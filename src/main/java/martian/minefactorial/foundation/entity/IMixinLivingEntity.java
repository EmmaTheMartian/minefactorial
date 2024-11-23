package martian.minefactorial.foundation.entity;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IMixinLivingEntity {
	void minefactorial$setShouldSkipDrops(boolean value);

	boolean minefactorial$shouldSkipDrops();
}
