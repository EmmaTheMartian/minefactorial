package martian.minefactorial.foundation.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;

public interface ITickableBE {
	default void serverTick(ServerLevel level) { }

	default void clientTick(ClientLevel level) { }
}
