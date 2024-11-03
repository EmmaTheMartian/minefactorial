package martian.minefactorial.datagen.client;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock;
import martian.minefactorial.foundation.pipenet.PipeState;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static martian.minefactorial.Minefactorial.id;

public class MFBlockStateProvider extends BlockStateProvider {
	public MFBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Minefactorial.MODID, exFileHelper);
	}

	private final ResourceLocation
			TEMPLATE_MACHINE = blockId("template_machine"),
			MACHINE_FRAME_SIDE = blockId("machine_frame_side"),
			MACHINE_FRAME_TOP = blockId("machine_frame_top"),
			MACHINE_FRAME_BOTTOM = blockId("machine_frame_bottom"),
			FLUID_OUTPUT_SIDE = blockId("fluid_output_side"),
			FLUID_OUTPUT_TOP = blockId("fluid_output_top"),
			FLUID_OUTPUT_BOTTOM = blockId("fluid_output_bottom"),
			ITEM_OUTPUT_SIDE = blockId("item_output_side"),
			ITEM_OUTPUT_TOP = blockId("item_output_top"),
			ITEM_OUTPUT_BOTTOM = blockId("item_output_bottom");

	@Override
	protected void registerStatesAndModels() {
		// Machines
		simpleMachineBlock(MFBlocks.STEAM_TURBINE, m -> m.texture("side", blockId("machine/steam_turbine_side")));
		simpleMachineBlock(MFBlocks.STEAM_BOILER, m -> m
				.texture("side", blockId("machine/steam_boiler_side"))
				.texture("up", FLUID_OUTPUT_TOP));
		specialDirectionalMachine(
				MFBlocks.BREAKER,
				models().withExistingParent(MFBlocks.BREAKER.getId().getPath(), TEMPLATE_MACHINE)
						.texture("north", blockId("machine/breaker_front"))
						.texture("south", ITEM_OUTPUT_SIDE),
				models().withExistingParent(MFBlocks.BREAKER.getId().getPath() + "_up", TEMPLATE_MACHINE)
						.texture("up", blockId("machine/breaker_up_front"))
						.texture("down", ITEM_OUTPUT_BOTTOM),
				models().withExistingParent(MFBlocks.BREAKER.getId().getPath() + "_down", TEMPLATE_MACHINE)
						.texture("down", blockId("machine/breaker_down_front"))
						.texture("up", ITEM_OUTPUT_TOP)
		);
		directionalMachine(MFBlocks.MOB_GRINDER, true, m -> m
				.texture("north", blockId("machine/mob_grinder_front"))
				.texture("south", ITEM_OUTPUT_SIDE));
		simpleMachineBlock(MFBlocks.FOUNTAIN, m -> m.texture("up", FLUID_OUTPUT_TOP));

		// Logistics
		simpleMachineBlock(MFBlocks.CAPACITOR, m -> m.texture("side", blockId("machine/capacitor_side")));
		simpleMachineBlock(MFBlocks.PLASTIC_TANK, m -> m.texture("side", blockId("machine/tank_side")));

		// Transport
		pipeBlock(MFBlocks.ENERGY_PIPE);
		pipeBlock(MFBlocks.FLUID_PIPE);
	}

	private void simpleMachineBlock(DeferredBlock<?> block, UnaryOperator<BlockModelBuilder> modelBuilderUnaryOperator) {
		simpleBlockWithItem(block.get(), modelBuilderUnaryOperator.apply(getMachineModel(block.getId().getPath())));
	}

	private void directionalMachine(DeferredBlock<?> block, boolean horizontalOnly, UnaryOperator<BlockModelBuilder> modelBuilderUnaryOperator) {
		BlockModelBuilder model = modelBuilderUnaryOperator.apply(getMachineModel(block.getId().getPath()));
		if (horizontalOnly) {
			horizontallyDirectionalBlock(block.get(), model);
		} else {
			directionalBlock(block.get(), model);
		}
		simpleBlockItem(block.get(), model);
	}

	private void specialDirectionalMachine(DeferredBlock<?> block, ModelFile modelHorizontal, ModelFile modelUp, ModelFile modelDown) {
		getVariantBuilder(block.get()).forAllStates(state -> {
			Direction dir = state.getValue(DirectionalBlock.FACING);
			return ConfiguredModel.builder()
					.modelFile(switch (dir) {
						case DOWN -> modelDown;
						case UP -> modelUp;
						default -> modelHorizontal;
					})
					.rotationY(((int) dir.toYRot() + 180) % 360)
					.build();
		});
		simpleBlockItem(block.get(), modelHorizontal);
	}

	private BlockModelBuilder getMachineModel(String id) {
		return models().withExistingParent(id, blockId("template_machine"));
	}

	private ResourceLocation blockId(String path) {
		return id("block/" + path);
	}

	// BlockState helpers
	public void horizontallyDirectionalBlock(Block block, ModelFile modelFile) {
		getVariantBuilder(block).forAllStates(state -> {
			Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
			return ConfiguredModel.builder()
					.modelFile(modelFile)
					.rotationY(((int) dir.toYRot() + 180) % 360)
					.build();
		});
	}

	// Thank you Thepigcat for letting me use this code! :D
	// https://github.com/Thepigcat76/Buildcraft-Legacy/blob/f9de2cf42727334d31670c462e0e5af96cc553fe/src/main/java/com/thepigcat/fancy_pipes/datagen/FPBlockStateProvider.java#L50
	private void pipeBlock(DeferredHolder<Block, ? extends Block> block) {
		ResourceLocation loc = block.getId();
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
		pipeConnection(builder, loc, Direction.UP, 180, 0);
		pipeConnection(builder, loc, Direction.DOWN, 0, 0);
		pipeConnection(builder, loc, Direction.NORTH, 90, 180);
		pipeConnection(builder, loc, Direction.EAST, 90, 270);
		pipeConnection(builder, loc, Direction.SOUTH, 90, 0);
		pipeConnection(builder, loc, Direction.WEST, 90, 90);
		builder.part().modelFile(pipeBaseModel(loc)).addModel().end();
	}

	private void pipeConnection(MultiPartBlockStateBuilder builder, ResourceLocation loc, Direction direction, int x, int y) {
		builder.part().modelFile(pipeConnectionModel(loc)).rotationX(x).rotationY(y).addModel()
				.condition(AbstractPipeBlock.getPropertyForDirection(direction), PipeState.CONNECTED).end()
				.part().modelFile(pipeBlockModel(loc)).rotationX(x).rotationY(y).addModel()
				.condition(AbstractPipeBlock.getPropertyForDirection(direction), PipeState.BLOCK).end();
	}

	private ModelFile pipeBaseModel(ResourceLocation loc) {
		return models().withExistingParent(loc.getPath() + "_base", id("block/pipe_base"))
				.texture("texture", id(loc.getNamespace(), "block/" + loc.getPath()));
	}

	private ModelFile pipeConnectionModel(ResourceLocation loc) {
		return models().withExistingParent(loc.getPath() + "_connection", id("block/pipe_connection"))
				.texture("texture", id(loc.getNamespace(), "block/" + loc.getPath()));
	}

	private ModelFile pipeBlockModel(ResourceLocation loc) {
		return models().withExistingParent(loc.getPath() + "_connection_block", id("block/pipe_connection_block"))
				.texture("texture", id(loc.getNamespace(), "block/" + loc.getPath() + "_block"));
	}
}
