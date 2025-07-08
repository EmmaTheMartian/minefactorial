package martian.minefactorial.content.block.logistics;

import martian.minefactorial.api.block.AbstractConveyorBlock;
import martian.minefactorial.api.block.IScrewdriverFunctionality;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHoppingConveyor extends AbstractConveyorBlock<BlockHoppingConveyorBE> implements IScrewdriverFunctionality {
	public BlockHoppingConveyor(Properties properties) {
		super(BlockHoppingConveyorBE::new, properties);
	}

	@Override
	public void onUseScrewdriver(Player player, ServerLevel level, BlockState state, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof BlockHoppingConveyorBE be) {
			if (be.maxInsert >= 64) {
				be.maxInsert = 1;
			} else {
				be.maxInsert = Math.clamp(be.maxInsert * 2L, 1, 64);
			}
			be.setChanged();
			player.sendSystemMessage(Component.translatable("messages.minefactorial.set_max_insert_to", be.maxInsert));
		}
	}
}
