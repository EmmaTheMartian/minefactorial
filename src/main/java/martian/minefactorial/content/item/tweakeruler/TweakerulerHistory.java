package martian.minefactorial.content.item.tweakeruler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.lazuli.api.collections.HistoryList;

import java.util.HashMap;
import java.util.Map;

public class TweakerulerHistory extends HistoryList<TweakerulerModeData> {
	protected Level level;
	public @Nullable TweakerulerModeData clipboard = null;

	public TweakerulerHistory(Level level, int maxHistoryLength) {
		super(maxHistoryLength);
		this.level = level;
	}

	@Override
	public void onUndo(TweakerulerModeData undoneItem) {
		undoneItem.statesBeforeAction().forEach(level::setBlockAndUpdate);
	}

	@Override
	public void onRedo(TweakerulerModeData redoneItem) {
		redoneItem.run();
	}

	public void run(Level level, BlockPos from, @Nullable BlockPos to, ItemStack rulerStack, ItemStack otherStack, @Nullable Player user, TweakerulerMode mode) {
		Map<BlockPos, BlockState> beforeTweaking = new HashMap<>();

		// When pasting, the size is equal to the copied size. We'll add `to` manually here so that we can have undo support for pasting.
		if (mode == TweakerulerMode.PASTE && clipboard != null) {
			BlockPos size = clipboard.to().offset(-clipboard.from().getX(), -clipboard.from().getY(), -clipboard.from().getZ());
			to = from.offset(size);
		}

		if (to != null) {
			BlockPos.betweenClosedStream(from, to).forEach(pos -> {
				BlockPos immutable = pos.immutable();
				beforeTweaking.put(immutable, level.getBlockState(immutable));
			});
		}

		TweakerulerModeData data = new TweakerulerModeData(
				this,
				beforeTweaking,
				from,
				to,
				rulerStack.copy(),
				otherStack.copy(),
				user,
				(ServerLevel)level,
				mode
		);

		int blocksChanged = this.run(data);
		if (user != null) {
			mode.notifyChangedBlocks(user, blocksChanged);
		}
	}

	protected int run(TweakerulerModeData action) {
		int affected = action.run();
		this.push(action);
		return affected;
	}
}
