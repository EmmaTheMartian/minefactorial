package martian.minefactorial.content.item.tweakeruler;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public enum TweakerulerMode implements StringRepresentable, TweakerulerAction.Callback {
	NONE((from, to, ruler, other, player, level) -> 0),
	PLACE((from, to, ruler, other, player, level) -> {
		Block replaceWith = other.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(from, to).forEach(pos -> {
			if (level.getBlockState(pos).isAir()) {
				level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
				blocksChanged.incrementAndGet();
			}
		});
		return blocksChanged.get();
	}),
	REPLACE((from, to, ruler, other, player, level) -> {
		Block replaceWith = other.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(from, to).forEach(pos -> {
			level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
			blocksChanged.incrementAndGet();
		});
		return blocksChanged.get();
	}),
	REMOVE((from, to, ruler, ignoredOther, player, level) -> {
		AtomicInteger blocksChanged = new AtomicInteger(0);
		BlockPos.betweenClosedStream(from, to).forEach(pos -> {
			if (!level.getBlockState(pos).isAir()) {
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				blocksChanged.incrementAndGet();
			}
		});
		return blocksChanged.get();
	}),
	;

	public static final Codec<TweakerulerMode> CODEC = StringRepresentable.fromEnum(TweakerulerMode::values);
	public static final StreamCodec<ByteBuf, TweakerulerMode> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	private final TweakerulerAction.Callback action;

	TweakerulerMode(TweakerulerAction.Callback action) {
		this.action = action;
	}

	public int run(BlockPos from, BlockPos to, ItemStack ruler, ItemStack other, Player player, Level level) {
		return this.action.run(from, to, ruler, other, player, level);
	}

	@Override
	public @NotNull String getSerializedName() {
		return this.name().toUpperCase();
	}
}
