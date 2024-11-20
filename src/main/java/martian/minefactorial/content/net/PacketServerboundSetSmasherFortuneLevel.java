package martian.minefactorial.content.net;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.menu.ContainerSmasher;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public record PacketServerboundSetSmasherFortuneLevel(int level) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<PacketServerboundSetSmasherFortuneLevel> TYPE = new Type<>(id("set_smasher_fortune_level"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerboundSetSmasherFortuneLevel> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, PacketServerboundSetSmasherFortuneLevel::level,
			PacketServerboundSetSmasherFortuneLevel::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketServerboundSetSmasherFortuneLevel packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			final Player player = context.player();
			if (player.containerMenu instanceof ContainerSmasher menu) {
				if (!menu.stillValid(player)) {
					Minefactorial.LOGGER.debug("Player {} interacted with invalid menu {}", player, menu);
					return;
				}
				menu.updateFortuneLevel(packet.level());
			}
		});
	}
}
