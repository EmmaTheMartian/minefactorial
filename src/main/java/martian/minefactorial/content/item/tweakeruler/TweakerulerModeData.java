package martian.minefactorial.content.item.tweakeruler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public record TweakerulerModeData(
		TweakerulerHistory history,
		Map<BlockPos, BlockState> statesBeforeAction,
		BlockPos from,
		BlockPos to,
		ItemStack rulerStack,
		ItemStack otherStack,
		Player user,
		ServerLevel level,
		TweakerulerMode mode
) {
	public int run() {
		return this.mode.action.apply(this);
	}
}
