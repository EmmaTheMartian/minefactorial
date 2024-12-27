package martian.minefactorial.content.net;

import martian.minefactorial.content.item.tweakeruler.TweakerulerHistory;
import martian.minefactorial.managers.TweakerulerHistoryManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.LOGGER;
import static martian.minefactorial.Minefactorial.id;

public record PacketServerboundTweakerulerRedo() implements CustomPacketPayload {
	public static final Type<PacketServerboundTweakerulerRedo> TYPE = new Type<>(id("tweakeruler_redo"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerboundTweakerulerRedo> STREAM_CODEC =
			StreamCodec.unit(new PacketServerboundTweakerulerRedo());

	@Override
	public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketServerboundTweakerulerRedo packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			final ServerPlayer player = (ServerPlayer) context.player();
			LOGGER.info("Redo requested for {}", player.getName());
			TweakerulerHistory history = TweakerulerHistoryManager.getHistoryFor(player);
			history.redo();
//			player.sendSystemMessage(Component.literal("Redid changes. data.size: %d, redoHistory.size: %d".formatted(history.dataSize(), history.historySize())));
		});
	}
}
