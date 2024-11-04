package martian.minefactorial.content.block.logistics;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockFluidExtractor extends AbstractBlockWithEntity<BlockFluidExtractorBE> {
	public static final DirectionProperty FACING = DirectionalBlock.FACING;

	private static final VoxelShape
			SHAPE_UP = Shapes.join(Block.box(1, 0, 1, 15, 10, 15), Block.box(2, 10, 2, 14, 16, 14), BooleanOp.OR),
			SHAPE_DOWN = Shapes.join(Block.box(1, 6, 1, 15, 16, 15), Block.box(2, 0, 2, 14, 6, 14), BooleanOp.OR),
			SHAPE_NORTH = Shapes.join(Block.box(1, 1, 6, 15, 15, 16), Block.box(2, 2, 0, 14, 14, 6), BooleanOp.OR),
			SHAPE_EAST = Shapes.join(Block.box(0, 1, 1, 10, 15, 15), Block.box(10, 2, 2, 16, 14, 14), BooleanOp.OR),
			SHAPE_SOUTH = Shapes.join(Block.box(1, 1, 0, 15, 15, 10), Block.box(2, 2, 10, 14, 14, 16), BooleanOp.OR),
			SHAPE_WEST = Shapes.join(Block.box(6, 1, 1, 16, 15, 15), Block.box(0, 2, 2, 6, 14, 14), BooleanOp.OR);

	public BlockFluidExtractor(Properties properties) {
		super(BlockFluidExtractorBE::new, properties);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP));
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
		builder.add(FACING);
	}

	@Override
	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> SHAPE_DOWN;
			case UP -> SHAPE_UP;
			case NORTH -> SHAPE_NORTH;
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			case EAST -> SHAPE_EAST;
		};
	}
}
