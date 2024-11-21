package martian.minefactorial.datagen.client

import martian.dapper.api.client.CubeModel
import martian.dapper.api.client.CubeModel.Companion.all
import martian.dapper.api.client.CubeModel.Companion.down
import martian.dapper.api.client.CubeModel.Companion.from
import martian.dapper.api.client.CubeModel.Companion.north
import martian.dapper.api.client.CubeModel.Companion.renderType
import martian.dapper.api.client.CubeModel.Companion.side
import martian.dapper.api.client.CubeModel.Companion.south
import martian.dapper.api.client.CubeModel.Companion.up
import martian.dapper.api.client.DapperBlockStateProvider
import martian.dapper.api.mcId
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.block.foliage.BlockRubberWood
import martian.minefactorial.content.block.machinery.BlockMacerator
import martian.minefactorial.content.block.power.BlockSteamBoiler
import martian.minefactorial.content.block.redstone.BlockRedstoneClock
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.datagen.id
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock
import martian.minefactorial.foundation.pipenet.PipeState
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.RotatedPillarBlock
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.DeferredBlock

class MFBlockStateProvider(event: GatherDataEvent) : DapperBlockStateProvider(event, Minefactorial.MODID) {
	private val machineFrameSide = blockId("machine_frame_side")
	private val machineFrameTop = blockId("machine_frame_top")
	private val machineFrameBottom = blockId("machine_frame_bottom")
	private val creativeMachineFrameSide = blockId("creative_machine_frame_side")
	private val creativeMachineFrameTop = blockId("creative_machine_frame_top")
	private val creativeMachineFrameBottom = blockId("creative_machine_frame_bottom")
	private val fluidOutputSide = blockId("fluid_output_side")
	private val fluidOutputTop = blockId("fluid_output_top")
	private val fluidOutputBottom = blockId("fluid_output_bottom")
	private val fluidInputSide = blockId("fluid_input_side")
	private val fluidInputTop = blockId("fluid_input_top")
	private val fluidInputBottom = blockId("fluid_input_bottom")
	private val itemOutputSide = blockId("item_output_side")
	private val itemOutputTop = blockId("item_output_top")
	private val itemOutputBottom = blockId("item_output_bottom")
	private val genericOutputSide = blockId("generic_output_side")
	private val genericOutputTop = blockId("generic_output_top")
	private val genericOutputBottom = blockId("generic_output_bottom")

	override fun registerStatesAndModels() {
		val machineFrame = (CubeModel()
			up machineFrameTop
			down machineFrameBottom
			side machineFrameSide)
		val creativeMachineFrame = (CubeModel()
			up creativeMachineFrameTop
			down creativeMachineFrameBottom
			side creativeMachineFrameSide)

		MFBlocks.STEAM_TURBINE.addModel(machineFrame.copy()
			side machineId("steam_turbine_side")
			up machineId("steam_turbine_top"))
		MFBlocks.FOUNTAIN.addModel(machineFrame.copy() up fluidOutputTop)
		MFBlocks.CAPACITOR.addModel(machineFrame.copy() side machineId("capacitor_side"))
		MFBlocks.PLASTIC_TANK.addModel(machineFrame.copy() side machineId("tank_side"))
		MFBlocks.CREATIVE_CAPACITOR.addModel(creativeMachineFrame.copy() side machineId("creative_capacitor_side"))
		MFBlocks.CREATIVE_TANK.addModel(creativeMachineFrame.copy() side machineId("creative_tank_side"))
		MFBlocks.STORAGE_UNIT.addModel(machineFrame.copy() side machineId("storage_unit_side"))
		MFBlocks.MACHINE_FRAME.addModel(machineFrame)

		MFBlocks.MOB_GRINDER.addHorizontalDirectionalModel(machineFrame.copy()
			north machineId("mob_grinder_front")
			south itemOutputSide)
		MFBlocks.SMASHER.addHorizontalDirectionalModel(machineFrame.copy()
			north machineId("smasher_front")
			south itemOutputSide)

		MFBlocks.FLUID_EXTRACTOR.addDirectionalModel(CubeModel() from blockId("fluid_extractor"))

		MFBlocks.BREAKER.addSpecialDirectionalModel(
			machineFrame.copy() north machineId("breaker_front") south itemOutputSide,
			machineFrame.copy() up machineId("breaker_up_front") down itemOutputBottom,
			machineFrame.copy() down machineId("breaker_down_front") up itemOutputTop
		)
		MFBlocks.PUMP.addSpecialDirectionalModel(
			machineFrame.copy() north fluidInputSide south fluidOutputSide,
			machineFrame.copy() up fluidInputTop down fluidOutputBottom,
			machineFrame.copy() down fluidInputBottom up fluidOutputTop
		)
		MFBlocks.PLACER.addSpecialDirectionalModel(
			machineFrame.copy() north genericOutputSide,
			machineFrame.copy() up genericOutputTop,
			machineFrame.copy() down genericOutputBottom
		)
		MFBlocks.EJECTOR.addSpecialDirectionalModel(
			machineFrame.copy() north itemOutputSide,
			machineFrame.copy() up itemOutputTop,
			machineFrame.copy() down itemOutputBottom,
		)

		dapperToggleState(
			MFBlocks.REDSTONE_CLOCK.get(),
			BlockRedstoneClock.POWERED,
			CubeModel() all blockId("redstone/redstone_clock_on"),
			CubeModel() all blockId("redstone/redstone_clock_off")
		)
		dapperToggleState(
			MFBlocks.STEAM_BOILER.get(),
			BlockSteamBoiler.LIT,
			machineFrame.copy()
				side machineId("steam_boiler_side_lit")
				up fluidOutputTop,
			machineFrame.copy()
				side machineId("steam_boiler_side")
				up fluidOutputTop
		)

		getVariantBuilder(MFBlocks.MACERATOR.get()).apply {
			val modelRunning = machineFrame.copy()
				.up(machineId("macerator_top_running"))
				.north(machineId("macerator_side_running"))
				.south(itemOutputSide)
				.build("macerator_running", models())
			val modelNotRunning = machineFrame.copy()
				.up(machineId("macerator_top"))
				.north(machineId("macerator_side"))
				.south(itemOutputSide)
				.build("macerator_not_running", models())

			MFBlocks.MACERATOR.get().stateDefinition.possibleStates.forEach { state ->
				val direction = state.getValue(BlockMacerator.FACING);
				val running = state.getValue(BlockMacerator.RUNNING);

				partialState()
					.with(BlockMacerator.FACING, direction)
					.with(BlockMacerator.RUNNING, running)
					.modelForState()
					.rotationY(((direction.toYRot() + 180) % 360).toInt())
					.modelFile(if (running) modelRunning else modelNotRunning)
					.addModel()
			}

			simpleBlockItem(MFBlocks.MACERATOR.get(), modelNotRunning)
		}

		// why is the datagen for rubber wood so cursed...
		// todo: dapper-ify this
		getVariantBuilder(MFBlocks.RUBBER_WOOD.get()).apply {
			// We build these models early so that we don't build any duplicates as we're going
			val modelVertical = CubeModel()
				.side(blockId("foliage/rubber_wood"))
				.up(blockId("foliage/rubber_wood_end"))
				.down(blockId("foliage/rubber_wood_end"))
				.build("rubber_wood_vertical", models())
			val modelHorizontal = CubeModel()
				.all(blockId("foliage/rubber_wood"))
				.north(blockId("foliage/rubber_wood_end"))
				.south(blockId("foliage/rubber_wood_end"))
				.build("rubber_wood_horizontal", models())
			val modelVerticalWithRubber = CubeModel()
				.withParent(modelVertical.uncheckedLocation)
				.side(blockId("foliage/rubber_wood_with_rubber"))
				.build("rubber_wood_vertical_with_rubber", models())
			val modelHorizontalWithRubber = CubeModel()
				.all(blockId("foliage/rubber_wood_with_rubber"))
				.north(blockId("foliage/rubber_wood_end"))
				.south(blockId("foliage/rubber_wood_end"))
				.build("rubber_wood_horizontal_with_rubber", models())

			partialState()
				// Non-rubbery variants
				.with(BlockRubberWood.HAS_RUBBER, false)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
				.modelForState()
				.modelFile(modelVertical)
				.addModel()
				.partialState()
				.with(BlockRubberWood.HAS_RUBBER, false)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
				.modelForState()
				.modelFile(modelHorizontal)
				.rotationX(90)
				.addModel()
				.partialState()
				.with(BlockRubberWood.HAS_RUBBER, false)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.X)
				.modelForState()
				.modelFile(modelHorizontal)
				.rotationX(90)
				.rotationY(90)
				.addModel()
				// Rubbery variants
				.partialState()
				.with(BlockRubberWood.HAS_RUBBER, true)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
				.modelForState()
				.modelFile(modelVerticalWithRubber)
				.addModel()
				.partialState()
				.with(BlockRubberWood.HAS_RUBBER, true)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
				.modelForState()
				.modelFile(modelHorizontalWithRubber)
				.rotationX(90)
				.addModel()
				.partialState()
				.with(BlockRubberWood.HAS_RUBBER, true)
				.with(RotatedPillarBlock.AXIS, Direction.Axis.X)
				.modelForState()
				.modelFile(modelHorizontalWithRubber)
				.rotationX(90)
				.rotationY(90)
				.addModel()

			simpleBlockItem(MFBlocks.RUBBER_WOOD.get(), modelVertical)
		}

		MFBlocks.RUBBER_LEAVES.addModel(CubeModel() all blockId("foliage/rubber_leaves") renderType "cutout".mcId)
		MFBlocks.RUBBER_SAPLING.addModel(CubeModel.crossModel(blockId("foliage/rubber_sapling")), makeItem = false)

		pipeBlock(MFBlocks.ENERGY_PIPE)
		pipeBlock(MFBlocks.FLUID_PIPE)

		MFBlocks.DECOR_BLOCKS.get("plastic_block").addModel(CubeModel() all blockId("decor/plastic_block"))
		MFBlocks.DECOR_BLOCKS.get("plastic_bricks").addModel(CubeModel() all blockId("decor/plastic_bricks"))
		MFBlocks.DECOR_BLOCKS.get("smooth_plastic").addModel(CubeModel() all blockId("decor/smooth_plastic"))
		MFBlocks.DECOR_BLOCKS.get("plastic_pillar") addAxisModel blockId("decor/plastic_pillar")

		MFBlocks.ROAD_BLOCKS.get("plastic_road").addModel(CubeModel() all blockId("decor/plastic_road"))
	}

	// Thank you Thepigcat for letting me use this code! :D
	// https://github.com/Thepigcat76/Buildcraft-Legacy/blob/f9de2cf42727334d31670c462e0e5af96cc553fe/src/main/java/com/thepigcat/fancy_pipes/datagen/FPBlockStateProvider.java#L50
	private fun pipeBlock(block: DeferredBlock<*>) {
		val loc = block.id
		val model = pipeBaseModel(loc)

		val builder = getMultipartBuilder(block.get())
		pipeConnection(builder, loc, Direction.UP, 180, 0)
		pipeConnection(builder, loc, Direction.DOWN, 0, 0)
		pipeConnection(builder, loc, Direction.NORTH, 90, 180)
		pipeConnection(builder, loc, Direction.EAST, 90, 270)
		pipeConnection(builder, loc, Direction.SOUTH, 90, 0)
		pipeConnection(builder, loc, Direction.WEST, 90, 90)
		builder.part().modelFile(model).addModel().end()

		simpleBlockItem(block.get(), model)
	}

	private fun pipeConnection(
		builder: MultiPartBlockStateBuilder,
		loc: ResourceLocation,
		direction: Direction,
		x: Int,
		y: Int
	) {
		builder.part().modelFile(pipeConnectionModel(loc)).rotationX(x).rotationY(y).addModel()
			.condition<PipeState>(
				AbstractPipeBlock.getPropertyForDirection(direction),
				PipeState.CONNECTED
			).end()
			.part().modelFile(pipeBlockModel(loc)).rotationX(x).rotationY(y).addModel()
			.condition<PipeState>(AbstractPipeBlock.getPropertyForDirection(direction), PipeState.BLOCK)
			.end()
	}

	private fun pipeBaseModel(loc: ResourceLocation): ModelFile {
		return models().withExistingParent("${loc.path}_base", "block/pipe_base".id)
			.texture("texture", Minefactorial.id(loc.namespace, "block/${loc.path}"))
	}

	private fun pipeConnectionModel(loc: ResourceLocation): ModelFile {
		return models().withExistingParent("${loc.path}_connection", "block/pipe_connection".id)
			.texture("texture", Minefactorial.id(loc.namespace, "block/${loc.path}"))
	}

	private fun pipeBlockModel(loc: ResourceLocation): ModelFile {
		return models().withExistingParent("${loc.path}_connection_block", "block/pipe_connection_block".id)
			.texture("texture", Minefactorial.id(loc.namespace, "block/${loc.path}_block"))
	}

	// ID Helpers
	private fun blockId(path: String) = "block/$path".id
	private fun machineId(path: String) = "block/machine/$path".id
}
