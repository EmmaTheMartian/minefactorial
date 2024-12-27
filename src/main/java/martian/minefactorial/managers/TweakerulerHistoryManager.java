package martian.minefactorial.managers;

import martian.minefactorial.content.item.tweakeruler.TweakerulerHistory;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TweakerulerHistoryManager {
	public static final int MAX_LENGTH = 10;

	private static final Map<UUID, TweakerulerHistory> playerHistories = new HashMap<>();

	private TweakerulerHistoryManager() { }

	public static void removeHistoryFor(ServerPlayer player) {
		playerHistories.remove(player.getUUID());
	}

	public static TweakerulerHistory getHistoryFor(ServerPlayer player) {
		if (!playerHistories.containsKey(player.getUUID())) {
			playerHistories.put(player.getUUID(), new TweakerulerHistory(player.level(), MAX_LENGTH));
		}
		return playerHistories.get(player.getUUID());
	}
}
