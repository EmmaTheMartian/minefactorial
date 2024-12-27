package martian.minefactorial.content.net;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.item.tweakeruler.TweakerulerMode;
import martian.minefactorial.content.menu.ContainerSmasher;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.content.registry.MFItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public record PacketServerboundSetTweakerulerMode(TweakerulerMode mode) implements CustomPacketPayload {
	public static final Type<PacketServerboundSetTweakerulerMode> TYPE = new Type<>(id("set_tweakeruler_mode"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerboundSetTweakerulerMode> STREAM_CODEC = StreamCodec.composite(
			TweakerulerMode.STREAM_CODEC, PacketServerboundSetTweakerulerMode::mode,
			PacketServerboundSetTweakerulerMode::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketServerboundSetTweakerulerMode packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			final Player player = context.player();
			ItemStack tweakeruler = player.getMainHandItem().is(MFItems.TWEAKERULER) ? player.getMainHandItem() : player.getOffhandItem();
			if (!tweakeruler.is(MFItems.TWEAKERULER)) {
				Minefactorial.LOGGER.debug("Player {} sent set_tweakeruler_mode but is not holding a tweakeruler", player);
				return;
			}
			tweakeruler.set(MFDataComponents.TWEAKERULER_MODE, packet.mode);
		});
	}
}
