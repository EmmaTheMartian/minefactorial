package martian.minefactorial.foundation.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ITickableBE {
	default void serverTick(ServerLevel level) { }

	@OnlyIn(Dist.CLIENT)
	default void clientTick(ClientLevel level) { }
}
