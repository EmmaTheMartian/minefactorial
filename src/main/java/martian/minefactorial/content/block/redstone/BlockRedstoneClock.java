package martian.minefactorial.content.block.redstone;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockRedstoneClock extends AbstractBlockWithEntity<BlockRedstoneClockBE> {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public BlockRedstoneClock(Properties properties) {
		super(BlockRedstoneClockBE::new, properties);
		registerDefaultState(getStateDefinition().any().setValue(POWERED, false));
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		return defaultBlockState().setValue(POWERED, false);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	@ParametersAreNonnullByDefault
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof BlockRedstoneClockBE redstoneClockBE) {
			if (player.isCrouching()) {
				redstoneClockBE.toggleTimeTicks -= 5;
			} else {
				redstoneClockBE.toggleTimeTicks += 5;
			}
			redstoneClockBE.ticksToNextToggle = redstoneClockBE.toggleTimeTicks;
			if (!level.isClientSide) {
				player.sendSystemMessage(Component.translatable("messages.minefactorial.set_ticks_to", redstoneClockBE.ticksToNextToggle));
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
