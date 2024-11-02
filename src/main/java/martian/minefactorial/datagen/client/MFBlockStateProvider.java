package martian.minefactorial.datagen.client;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock;
import martian.minefactorial.foundation.pipenet.PipeState;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.UnaryOperator;

import static martian.minefactorial.Minefactorial.id;

public class MFBlockStateProvider extends BlockStateProvider {
	public MFBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Minefactorial.MODID, exFileHelper);
	}

	private final ResourceLocation
			MACHINE_FRAME_SIDE = blockId("machine_frame_side"),
			MACHINE_FRAME_TOP = blockId("machine_frame_top"),
			MACHINE_FRAME_BOTTOM = blockId("machine_frame_bottom"),
			FLUID_OUTPUT_TOP = blockId("fluid_output_top"),
			ITEM_OUTPUT_SIDE = blockId("item_output_side");

	@Override
	protected void registerStatesAndModels() {
		// Machines
		simpleMachineBlock(MFBlocks.STEAM_TURBINE, m -> m.texture("side", blockId("machine/steam_turbine_side")));
		simpleMachineBlock(MFBlocks.STEAM_BOILER, m -> m
				.texture("side", blockId("machine/steam_boiler_side"))
				.texture("up", FLUID_OUTPUT_TOP));
		simpleMachineBlock(MFBlocks.BREAKER, m -> m
				.texture("north", blockId("machine/breaker_front"))
				.texture("south", ITEM_OUTPUT_SIDE));
		simpleMachineBlock(MFBlocks.MOB_GRINDER, m -> m
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

	private BlockModelBuilder getMachineModel(String id) {
		return models().cubeBottomTop(id, MACHINE_FRAME_SIDE, MACHINE_FRAME_BOTTOM, MACHINE_FRAME_TOP);
	}

	private ResourceLocation blockId(String path) {
		return id("block/" + path);
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
