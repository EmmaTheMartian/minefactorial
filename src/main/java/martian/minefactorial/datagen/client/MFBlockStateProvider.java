package martian.minefactorial.datagen.client;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFBlocks;
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock;
import martian.minefactorial.foundation.pipenet.PipeState;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

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
			FLUID_INPUT_SIDE = blockId("fluid_input_side"),
			FLUID_INPUT_TOP = blockId("fluid_input_top"),
			FLUID_INPUT_BOTTOM = blockId("fluid_input_bottom"),
			ITEM_OUTPUT_SIDE = blockId("item_output_side"),
			ITEM_OUTPUT_TOP = blockId("item_output_top"),
			ITEM_OUTPUT_BOTTOM = blockId("item_output_bottom"),
			GENERIC_OUTPUT_SIDE = blockId("generic_output_side"),
			GENERIC_OUTPUT_TOP = blockId("generic_output_top"),
			GENERIC_OUTPUT_BOTTOM = blockId("generic_output_bottom");

	@Override
	protected void registerStatesAndModels() {
		simpleBlockWithItem(MFBlocks.STEAM_TURBINE.get(), getMachineModel(MFBlocks.STEAM_TURBINE)
				.texture("side", blockId("machine/steam_turbine_side")));
		simpleBlockWithItem(MFBlocks.STEAM_BOILER.get(), getMachineModel(MFBlocks.STEAM_BOILER)
				.texture("side", blockId("machine/steam_boiler_side"))
				.texture("up", FLUID_OUTPUT_TOP));
		simpleBlockWithItem(MFBlocks.FOUNTAIN.get(), getMachineModel(MFBlocks.FOUNTAIN)
				.texture("up", FLUID_OUTPUT_TOP));
		simpleBlockWithItem(MFBlocks.CAPACITOR.get(), getMachineModel(MFBlocks.CAPACITOR)
				.texture("side", blockId("machine/capacitor_side")));
		simpleBlockWithItem(MFBlocks.PLASTIC_TANK.get(), getMachineModel(MFBlocks.PLASTIC_TANK)
				.texture("side", blockId("machine/tank_side")));
		simpleBlockWithItem(MFBlocks.CREATIVE_CAPACITOR.get(), getCreativeMachineModel(MFBlocks.CREATIVE_CAPACITOR)
				.texture("side", blockId("machine/creative_capacitor_side")));
		simpleBlockWithItem(MFBlocks.CREATIVE_TANK.get(), getCreativeMachineModel(MFBlocks.CREATIVE_TANK)
				.texture("side", blockId("machine/creative_tank_side")));

		horizontallyDirectionalBlockWithItem(MFBlocks.MOB_GRINDER, getMachineModel(MFBlocks.MOB_GRINDER)
				.texture("north", blockId("machine/mob_grinder_front"))
				.texture("south", ITEM_OUTPUT_SIDE));

		directionalBlockWithItem(MFBlocks.FLUID_EXTRACTOR, new ModelFile.UncheckedModelFile(blockId("fluid_extractor")));

		specialDirectionalMachine(
				MFBlocks.BREAKER,
				getMachineModel(MFBlocks.BREAKER)
						.texture("north", blockId("machine/breaker_front"))
						.texture("south", ITEM_OUTPUT_SIDE),
				getMachineModel(MFBlocks.BREAKER, "_up")
						.texture("up", blockId("machine/breaker_up_front"))
						.texture("down", ITEM_OUTPUT_BOTTOM),
				getMachineModel(MFBlocks.BREAKER, "_down")
						.texture("down", blockId("machine/breaker_down_front"))
						.texture("up", ITEM_OUTPUT_TOP)
		);
		specialDirectionalMachine(
				MFBlocks.PUMP,
				getMachineModel(MFBlocks.PUMP)
						.texture("north", FLUID_INPUT_SIDE)
						.texture("south", FLUID_OUTPUT_SIDE),
				getMachineModel(MFBlocks.PUMP, "_up")
						.texture("up", FLUID_INPUT_TOP)
						.texture("down", FLUID_OUTPUT_BOTTOM),
				getMachineModel(MFBlocks.PUMP, "_down")
						.texture("down", FLUID_INPUT_BOTTOM)
						.texture("up", FLUID_OUTPUT_TOP)
		);
		specialDirectionalMachine(
				MFBlocks.PLACER,
				getMachineModel(MFBlocks.PUMP).texture("north", GENERIC_OUTPUT_SIDE),
				getMachineModel(MFBlocks.PUMP, "_up").texture("up", GENERIC_OUTPUT_TOP),
				getMachineModel(MFBlocks.PUMP, "_down").texture("down", GENERIC_OUTPUT_BOTTOM)
		);

		pipeBlock(MFBlocks.ENERGY_PIPE);
		pipeBlock(MFBlocks.FLUID_PIPE);
	}

	// ID and model shorthands
	private BlockModelBuilder getMachineModel(String id) {
		return models().withExistingParent(id, TEMPLATE_MACHINE);
	}

	private BlockModelBuilder getCreativeMachineModel(String id) {
		return models().withExistingParent(id, blockId("template_machine_creative"));
	}

	private BlockModelBuilder getMachineModel(DeferredBlock<?> block) {
		return models().withExistingParent(block.getId().getPath(), blockId("template_machine"));
	}

	private BlockModelBuilder getCreativeMachineModel(DeferredBlock<?> block) {
		return models().withExistingParent(block.getId().getPath(), blockId("template_machine_creative"));
	}

	private BlockModelBuilder getMachineModel(DeferredBlock<?> block, String idAffix) {
		return models().withExistingParent(block.getId().getPath() + idAffix, blockId("template_machine"));
	}

	private BlockModelBuilder getCreativeMachineModel(DeferredBlock<?> block, String idAffix) {
		return models().withExistingParent(block.getId().getPath() + idAffix, blockId("template_machine_creative"));
	}

	private ResourceLocation blockId(String path) {
		return id("block/" + path);
	}

	// State and Model Helpers
	private void horizontallyDirectionalBlockWithItem(DeferredBlock<?> block, ModelFile model) {
		horizontalBlock(block.get(), model);
		simpleBlockItem(block.get(), model);
	}

	private void directionalBlockWithItem(DeferredBlock<?> block, ModelFile model) {
		directionalBlock(block.get(), model);
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
