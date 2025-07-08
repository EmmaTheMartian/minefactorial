package martian.minefactorial.content.block.redstone;

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
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockRedstoneClock extends AbstractBlockWithEntity<BlockRedstoneClockBE> {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final int TIME_MIN = 5, TIME_MAX = 240;

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
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		boolean hasSignal = level.getDirectSignalTo(pos) > 0;
		if (level.getBlockEntity(pos) instanceof BlockRedstoneClockBE be) {
			if (be.locked != hasSignal) {
				be.locked = hasSignal;
				be.ticksToNextToggle = be.toggleTimeTicks;
				be.setChanged();
			}
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof BlockRedstoneClockBE redstoneClockBE) {
			redstoneClockBE.toggleTimeTicks = Math.clamp(
					redstoneClockBE.toggleTimeTicks + (player.isCrouching() ? -5 : 5),
					TIME_MIN,
					TIME_MAX
			);
			redstoneClockBE.ticksToNextToggle = redstoneClockBE.toggleTimeTicks;
			if (!level.isClientSide) {
				player.sendSystemMessage(Component.translatable("messages.minefactorial.set_ticks_to", redstoneClockBE.ticksToNextToggle));
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
