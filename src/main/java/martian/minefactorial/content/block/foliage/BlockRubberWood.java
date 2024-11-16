package martian.minefactorial.content.block.foliage;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

import static martian.minefactorial.Minefactorial.id;

public class BlockRubberWood extends RotatedPillarBlock {
	public static final BooleanProperty HAS_RUBBER = BooleanProperty.create("has_rubber");

	public BlockRubberWood(Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(HAS_RUBBER, false).setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(HAS_RUBBER, false)
				.setValue(RotatedPillarBlock.AXIS, context.getClickedFace().getAxis());
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HAS_RUBBER, RotatedPillarBlock.AXIS);
	}

	public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, id("rubber_tree"));

	public static final TreeGrower TREE_GROWER = new TreeGrower(
			"rubber_tree",
			0f,
			Optional.empty(),
			Optional.empty(),
			Optional.of(RUBBER_TREE),
			Optional.empty(),
			Optional.empty(),
			Optional.empty()
	);
}
