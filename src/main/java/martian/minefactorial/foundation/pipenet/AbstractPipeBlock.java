package martian.minefactorial.foundation.pipenet;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import martian.minefactorial.foundation.block.BlockEntityFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

// Model/shape code adapted from https://github.com/McJty/Tut4_3Power/blob/ca152c3644ed0073a00ffdad5b11655c73eb396d/src/main/java/com/mcjty/tutpower/cables/blocks/CableBlock.java#L37
public abstract class AbstractPipeBlock<T, U extends BlockEntity> extends AbstractBlockWithEntity<U> implements EntityBlock {
	public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<PipeState>
			CONNECTION_UP = EnumProperty.create("up", PipeState.class),
			CONNECTION_DOWN = EnumProperty.create("down", PipeState.class),
			CONNECTION_NORTH = EnumProperty.create("north", PipeState.class),
			CONNECTION_EAST = EnumProperty.create("east", PipeState.class),
			CONNECTION_SOUTH = EnumProperty.create("south", PipeState.class),
			CONNECTION_WEST = EnumProperty.create("west", PipeState.class);

	private static final float
			PS = 12f/16f, // Cable size
			PO = 1f - PS, // The offset for the cable
			BS = 12f/16f, // "Block connection indicator" size
			BO = 1f - BS // "Block connection indicator" offset
	;
	// Cable shapes
	private static final VoxelShape
			SHAPE_CABLE_NORTH = Shapes.box(PO, PO,  0, PS, PS, PO),
			SHAPE_CABLE_SOUTH = Shapes.box(PO, PO, PS, PS, PS,  1),
			SHAPE_CABLE_WEST  = Shapes.box( 0, PO, PO, PO, PS, PS),
			SHAPE_CABLE_EAST  = Shapes.box(PS, PO, PO,  1, PS, PS),
			SHAPE_CABLE_UP    = Shapes.box(PO, PS, PO, PS,  1, PS),
			SHAPE_CABLE_DOWN  = Shapes.box(PO,  0, PO, PS, PO, PS),
			SHAPE_BLOCK_NORTH = Shapes.box(BO, BO,  0, BS, BS, .1),
			SHAPE_BLOCK_SOUTH = Shapes.box(BO, BO, .9, BS, BS,  1),
			SHAPE_BLOCK_WEST  = Shapes.box( 0, BO, BO, .1, BS, BS),
			SHAPE_BLOCK_EAST  = Shapes.box(.9, BO, BO,  1, BS, BS),
			SHAPE_BLOCK_UP    = Shapes.box(BO, .9, BO, BS,  1, BS),
			SHAPE_BLOCK_DOWN  = Shapes.box(BO,  0, BO, BS, .1, BS);

	private static final VoxelShape[] shapeCache = makeShapes();

	private final BlockCapability<T, @Nullable Direction> transferCapability;
	private final DeferredBlock<?> blockHolder;

	public AbstractPipeBlock(
			BlockEntityFactory<U> blockEntitySupplier,
			DeferredBlock<?> blockHolder,
			BlockCapability<T, @Nullable Direction> transferCapability,
			Properties properties
	) {
		super(blockEntitySupplier, properties);

		this.blockHolder = blockHolder;
		this.transferCapability = transferCapability;

		registerDefaultState(getStateDefinition().any()
				.setValue(WATERLOGGED, false)
				.setValue(CONNECTION_UP, PipeState.DISCONNECTED)
				.setValue(CONNECTION_DOWN, PipeState.DISCONNECTED)
				.setValue(CONNECTION_NORTH, PipeState.DISCONNECTED)
				.setValue(CONNECTION_EAST, PipeState.DISCONNECTED)
				.setValue(CONNECTION_SOUTH, PipeState.DISCONNECTED)
				.setValue(CONNECTION_WEST, PipeState.DISCONNECTED));
	}

	// Non-Override Methods
	public BlockState getConnectionsForState(LevelAccessor level, BlockPos pos, BlockState state) {
		return state
				.setValue(CONNECTION_UP, getConnectionTo(level, pos, Direction.UP))
				.setValue(CONNECTION_DOWN, getConnectionTo(level, pos, Direction.DOWN))
				.setValue(CONNECTION_NORTH, getConnectionTo(level, pos, Direction.NORTH))
				.setValue(CONNECTION_EAST, getConnectionTo(level, pos, Direction.EAST))
				.setValue(CONNECTION_SOUTH, getConnectionTo(level, pos, Direction.SOUTH))
				.setValue(CONNECTION_WEST, getConnectionTo(level, pos, Direction.WEST));
	}

	public PipeState getConnectionTo(LevelAccessor level, BlockPos pipePos, Direction facing) {
		BlockPos relativePos = pipePos.relative(facing);
		BlockState state = level.getBlockState(relativePos);

		if (state.isAir()) {
			return PipeState.DISCONNECTED;
		} else if (state.is(blockHolder.get())) {
			return PipeState.CONNECTED;
		} else {
			BlockEntity blockEntity = level.getBlockEntity(relativePos);
			if (blockEntity != null) {
				Level realLevel = blockEntity.getLevel();
				if (realLevel != null && realLevel.getCapability(this.transferCapability, relativePos, facing) != null) {
					return PipeState.BLOCK;
				}
			}
		}

		return PipeState.DISCONNECTED;
	}

	// Overrides
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		return getConnectionsForState(level, pos, defaultBlockState())
				.setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(
				WATERLOGGED,
				CONNECTION_UP,
				CONNECTION_DOWN,
				CONNECTION_NORTH,
				CONNECTION_EAST,
				CONNECTION_SOUTH,
				CONNECTION_WEST
		);
	}

	@Override
	public @NotNull FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			level.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, pos, Fluids.WATER.getTickDelay(level), 0L));
		}
		return getConnectionsForState(level, pos, state);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		BlockState newState = getConnectionsForState(level, pos, state);
		if (state != newState) {
			level.setBlockAndUpdate(pos, newState);
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		PipeState up = AbstractPipeBlock.getStateForDirection(state, Direction.UP);
		PipeState down = AbstractPipeBlock.getStateForDirection(state, Direction.DOWN);
		PipeState north = AbstractPipeBlock.getStateForDirection(state, Direction.NORTH);
		PipeState east = AbstractPipeBlock.getStateForDirection(state, Direction.EAST);
		PipeState south = AbstractPipeBlock.getStateForDirection(state, Direction.SOUTH);
		PipeState west = AbstractPipeBlock.getStateForDirection(state, Direction.WEST);
		return shapeCache[calculateShapeIndex(up, down, north, east, south, west)];
	}

	@Override
	@ParametersAreNonnullByDefault
	protected @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getShape(state, level, pos, context);
	}

	// Static methods
	public static PipeState getStateForDirection(BlockState state, Direction direction) {
		return switch (direction) {
			case Direction.UP -> state.getValue(CONNECTION_UP);
			case Direction.DOWN -> state.getValue(CONNECTION_DOWN);
			case Direction.NORTH -> state.getValue(CONNECTION_NORTH);
			case Direction.EAST -> state.getValue(CONNECTION_EAST);
			case Direction.SOUTH -> state.getValue(CONNECTION_SOUTH);
			case Direction.WEST -> state.getValue(CONNECTION_WEST);
		};
	}

	public static EnumProperty<PipeState> getPropertyForDirection(Direction direction) {
		return switch (direction) {
			case Direction.UP -> CONNECTION_UP;
			case Direction.DOWN -> CONNECTION_DOWN;
			case Direction.NORTH -> CONNECTION_NORTH;
			case Direction.EAST -> CONNECTION_EAST;
			case Direction.SOUTH -> CONNECTION_SOUTH;
			case Direction.WEST -> CONNECTION_WEST;
		};
	}

	private static int calculateShapeIndex(PipeState up, PipeState down, PipeState north, PipeState east, PipeState south, PipeState west) {
		int l = PipeState.values().length;
		return ((((south.ordinal() * l + north.ordinal()) * l + west.ordinal()) * l + east.ordinal()) * l + up.ordinal()) * l + down.ordinal();
	}

	@SuppressWarnings("SuspiciousIndentAfterControlStatement")
	private static VoxelShape[] makeShapes() {
		int length = PipeState.values().length;
		VoxelShape[] shapeCache = new VoxelShape[(int) Math.pow(length, 6)];

		for (PipeState up : PipeState.values())
		for (PipeState down : PipeState.values())
		for (PipeState north : PipeState.values())
		for (PipeState east : PipeState.values())
		for (PipeState south : PipeState.values())
		for (PipeState west : PipeState.values()) {
			int index = calculateShapeIndex(up, down, north, east, south, west);
			shapeCache[index] = makeShape(up, down, north, east, south, west);
		}

		return shapeCache;
	}

	private static VoxelShape makeShape(PipeState up, PipeState down, PipeState north, PipeState east, PipeState south, PipeState west) {
		VoxelShape shape = Shapes.box(PO, PO, PO, PS, PS, PS);
		shape = combineShape(shape, up, SHAPE_CABLE_UP, SHAPE_BLOCK_UP);
		shape = combineShape(shape, down, SHAPE_CABLE_DOWN, SHAPE_BLOCK_DOWN);
		shape = combineShape(shape, north, SHAPE_CABLE_NORTH, SHAPE_BLOCK_NORTH);
		shape = combineShape(shape, east, SHAPE_CABLE_EAST, SHAPE_BLOCK_EAST);
		shape = combineShape(shape, south, SHAPE_CABLE_SOUTH, SHAPE_BLOCK_SOUTH);
		shape = combineShape(shape, west, SHAPE_CABLE_WEST, SHAPE_BLOCK_WEST);
		return shape;
	}

	private static VoxelShape combineShape(VoxelShape shape, PipeState pipeState, VoxelShape cableShape, VoxelShape blockShape) {
		return switch (pipeState) {
			case DISCONNECTED -> shape;
			case CONNECTED -> Shapes.join(shape, cableShape, BooleanOp.OR);
			case BLOCK -> Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
		};
	}
}
