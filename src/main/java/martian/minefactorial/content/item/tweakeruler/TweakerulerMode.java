package martian.minefactorial.content.item.tweakeruler;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TweakerulerMode implements StringRepresentable {
	NONE(data -> 0),
	PLACE(data -> {
		Block replaceWith = data.otherStack().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(data.from(), data.to()).forEach(pos -> {
			if (data.level().getBlockState(pos).isAir()) {
				data.level().setBlockAndUpdate(pos, replaceWith.defaultBlockState());
				blocksChanged.incrementAndGet();
			}
		});
		return blocksChanged.get();
	}),
	REPLACE(data -> {
		Block replaceWith = data.otherStack().getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(data.from(), data.to()).forEach(pos -> {
			data.level().setBlockAndUpdate(pos, replaceWith.defaultBlockState());
			blocksChanged.incrementAndGet();
		});
		return blocksChanged.get();
	}),
	REMOVE(data -> {
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(data.from(), data.to()).forEach(pos -> {
			if (!data.level().getBlockState(pos).isAir()) {
				data.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				blocksChanged.incrementAndGet();
			}
		});
		return blocksChanged.get();
	}),
	COPY(data -> {
		BlockPos origin = data.from();
		Map<BlockPos, BlockState> states = new HashMap<>();
		data.statesBeforeAction().forEach((key, value) -> states.put(key.offset(-origin.getX(), -origin.getY(), -origin.getZ()), value));
		data.history().clipboard = new TweakerulerModeData(
				data.history(),
				states,
				data.from(),
				data.to(),
				data.rulerStack(),
				data.otherStack(),
				data.user(),
				data.level(),
				data.mode()
		);
		return states.size();
	}) {
		@Override
		public void notifyChangedBlocks(Player player, int blocksChanged) {
			player.sendSystemMessage(Component.translatable("messages.minefactorial.copied_blocks",
					Component.literal(String.valueOf(blocksChanged)).withStyle(ChatFormatting.RED)));
		}
	},
	PASTE(data -> {
		var clipboard = data.history().clipboard;
		if (clipboard == null) {
			return -1;
		}
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos origin = data.from();
		BlockPos size = clipboard.to().offset(-clipboard.from().getX(), -clipboard.from().getY(), -clipboard.from().getZ());
		BlockPos.betweenClosedStream(data.from(), data.from().offset(size)).forEach(pos -> {
			var relativeToClipboardOrigin = pos.offset(-origin.getX(), -origin.getY(), -origin.getZ());
			data.level().setBlockAndUpdate(pos, clipboard.statesBeforeAction().get(relativeToClipboardOrigin));
			blocksChanged.incrementAndGet();
		});
		return blocksChanged.get();
	})
	;

	static {
		PLACE.offsetPosByFace = true;
		PASTE.offsetPosByFace = true;
		PASTE.singlePos = true;
	}

	public static final Codec<TweakerulerMode> CODEC = StringRepresentable.fromEnum(TweakerulerMode::values);
	public static final StreamCodec<ByteBuf, TweakerulerMode> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public final Function<TweakerulerModeData, Integer> action;
	// When true, the selected blocks (from/to) will be offset relative to the face they were clicked on, as if placing a block.
	public boolean offsetPosByFace = false;
	// When true, only a single position will be selected.
	public boolean singlePos = false;

	TweakerulerMode(Function<TweakerulerModeData, Integer> action) {
		this.action = action;
	}

	public void notifyChangedBlocks(Player player, int blocksChanged) {
		player.sendSystemMessage(Component.translatable("messages.minefactorial.blocks_changed",
				Component.literal(String.valueOf(blocksChanged)).withStyle(ChatFormatting.RED)));
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name().toUpperCase();
	}
}
