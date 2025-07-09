package martian.minefactorial.content.net;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.machinery.husbandry.BlockChronotyperBE;
import martian.minefactorial.content.menu.ContainerChronotyper;
import martian.minefactorial.content.menu.ContainerSmasher;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static martian.minefactorial.Minefactorial.id;

public record PacketServerboundSetChronotyperState(int state) implements CustomPacketPayload {
	public static final Type<PacketServerboundSetChronotyperState> TYPE = new Type<>(id("set_chronotyper_state"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketServerboundSetChronotyperState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, PacketServerboundSetChronotyperState::state,
			PacketServerboundSetChronotyperState::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketServerboundSetChronotyperState packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			final Player player = context.player();
			if (player.containerMenu instanceof ContainerChronotyper menu) {
				if (!menu.stillValid(player)) {
					Minefactorial.LOGGER.debug("Player {} interacted with invalid menu {}", player, menu);
					return;
				}
				menu.setMoveState(BlockChronotyperBE.MoveState.values()[packet.state()]);
			}
		});
	}
}
