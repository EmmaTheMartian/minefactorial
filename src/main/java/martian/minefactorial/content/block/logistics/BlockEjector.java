package martian.minefactorial.content.block.logistics;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import martian.minefactorial.foundation.block.IInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockEjector extends AbstractBlockWithEntity<BlockEjectorBE> {
	public static final int SLOTS = 9;
	private static final int TRIGGER_DURATION = 4;

	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	public BlockEjector(Properties properties) {
		super(BlockEjectorBE::new, properties);
		registerDefaultState(getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(TRIGGERED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		//noinspection deprecation
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TRIGGERED);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		boolean hasSignal = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
		boolean isTriggered = state.getValue(TRIGGERED);
		if (hasSignal && !isTriggered) {
			level.scheduleTick(pos, this, TRIGGER_DURATION);
			level.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
		} else if (!hasSignal && isTriggered) {
			level.setBlock(pos, state.setValue(TRIGGERED, false), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockEjectorBE be) {
			player.openMenu(new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() {
					return Component.literal("Ejector");
				}

				@Override
				public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
					return new DispenserMenu(windowId, inventory, be);
				}
			});
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.getBlockEntity(pos) instanceof BlockEjectorBE ejectorBE) {
			IInventoryBE.ejectFrom(ejectorBE, 1);
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@ParametersAreNonnullByDefault
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}
}
