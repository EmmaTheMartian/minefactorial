package martian.minefactorial.content.item.tweakeruler;

import martian.minefactorial.foundation.HistoryList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TweakerulerHistory extends HistoryList<TweakerulerAction> {
	protected Level level;

	public TweakerulerHistory(Level level, int maxHistoryLength) {
		super(maxHistoryLength);
		this.level = level;
	}

	@Override
	public void onUndo(TweakerulerAction undoneItem) {
		undoneItem.statesBeforeAction().forEach(level::setBlockAndUpdate);
	}

	@Override
	public void onRedo(TweakerulerAction redoneItem) {
		redoneItem.run();
	}

	public int run(Level level, BlockPos from, BlockPos to, ItemStack rulerStack, ItemStack otherStack, @Nullable Player user, TweakerulerAction.Callback actionCallback) {
		Map<BlockPos, BlockState> beforeTweaking = new HashMap<>();
		BlockPos.betweenClosedStream(from, to).forEach(pos -> {
			BlockPos immutable = pos.immutable();
			beforeTweaking.put(immutable, level.getBlockState(immutable));
		});
		TweakerulerAction action = new TweakerulerAction(beforeTweaking, from, to, rulerStack.copy(), otherStack.copy(), user, actionCallback);
		return this.run(action);
	}

	protected int run(TweakerulerAction action) {
		int affected = action.run();
		this.push(action);
		return affected;
	}
}
