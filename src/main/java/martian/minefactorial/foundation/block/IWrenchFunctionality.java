package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface IWrenchFunctionality {
	void onWrenched(Player player, ServerLevel level, BlockState state, BlockPos pos);
}
