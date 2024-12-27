package martian.minefactorial.content.item.tweakeruler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public record TweakerulerAction(
		Map<BlockPos, BlockState> statesBeforeAction,
		BlockPos from,
		BlockPos to,
		ItemStack rulerStack,
		ItemStack otherStack,
		Player user,
		Callback action
) {
	public int run() {
		return this.action.run(from, to, rulerStack, otherStack, user, user.level());
	}

	@FunctionalInterface
	public interface Callback {
		int run(BlockPos from, BlockPos to, ItemStack ruler, ItemStack other, Player player, Level level);
	}
}
