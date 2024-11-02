package martian.minefactorial.content.block;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import martian.minefactorial.foundation.block.IWrenchFunctionality;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockConveyor extends AbstractBlockWithEntity<BlockConveyorBE> implements IWrenchFunctionality {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<VerticalState> VERTICAL_STATE = EnumProperty.create("vertical", VerticalState.class);

	protected static final VoxelShape FLAT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	protected static final VoxelShape FULL_BLOCK_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

	public BlockConveyor(Properties properties) {
		super(BlockConveyorBE::new, properties);
		registerDefaultState(getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(VERTICAL_STATE, VerticalState.NONE));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(VERTICAL_STATE, VerticalState.NONE);
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
		builder.add(FACING, VERTICAL_STATE);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		VerticalState verticalState = state.getValue(VERTICAL_STATE);
		return verticalState != VerticalState.NONE ? FULL_BLOCK_AABB : FLAT_AABB;
	}

	@Override
	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(VERTICAL_STATE) == VerticalState.NONE) {
			return FLAT_AABB;
		}
		return Shapes.empty();
	}

	@Override
	public void onWrenched(Player player, ServerLevel level, BlockState state, BlockPos pos) {
		level.setBlockAndUpdate(pos, state.setValue(VERTICAL_STATE, VerticalState.next(state.getValue(VERTICAL_STATE))));
	}

	public enum VerticalState implements StringRepresentable {
		NONE,
		UP,
		DOWN,
		;

		@Override
		public @NotNull String getSerializedName() {
			return this.name().toLowerCase();
		}

		public static VerticalState next(VerticalState state) {
			return switch (state) {
				case NONE -> UP;
				case UP -> DOWN;
				case DOWN -> NONE;
			};
		}
	}
}
