package martian.minefactorial.datagen.client

import martian.minefactorial.Minefactorial
import martian.minefactorial.content.block.redstone.BlockRedstoneClock
import martian.minefactorial.content.registry.MFBlocks
import martian.dapper.api.client.CubeModel
import martian.dapper.api.client.CubeModel.Companion.from
import martian.dapper.api.client.CubeModel.Companion.all
import martian.dapper.api.client.CubeModel.Companion.side
import martian.dapper.api.client.CubeModel.Companion.up
import martian.dapper.api.client.CubeModel.Companion.down
import martian.dapper.api.client.CubeModel.Companion.north
import martian.dapper.api.client.CubeModel.Companion.south
import martian.dapper.api.client.DapperBlockStateProvider
import martian.minefactorial.datagen.id
import martian.minefactorial.foundation.pipenet.AbstractPipeBlock
import martian.minefactorial.foundation.pipenet.PipeState
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.*
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

		MFBlocks.STEAM_TURBINE.addModel(machineFrame.copy() side machineId("steam_turbine_side"))
		MFBlocks.STEAM_BOILER.addModel(machineFrame.copy()
			side machineId("steam_boiler_side")
			up fluidOutputTop)
		MFBlocks.FOUNTAIN.addModel(machineFrame.copy() up fluidOutputTop)
		MFBlocks.CAPACITOR.addModel(machineFrame.copy() side machineId("capacitor_side"))
		MFBlocks.PLASTIC_TANK.addModel(machineFrame.copy() side machineId("tank_side"))
		MFBlocks.CREATIVE_CAPACITOR.addModel(creativeMachineFrame.copy() side machineId("creative_capacitor_side"))
		MFBlocks.CREATIVE_TANK.addModel(creativeMachineFrame.copy() side machineId("creative_tank_side"))
		MFBlocks.STORAGE_UNIT.addModel(machineFrame.copy() side machineId("storage_unit_side"))

		MFBlocks.MOB_GRINDER.addHorizontalDirectionalModel(machineFrame.copy()
			north machineId("mob_grinder_front")
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

		dapperToggleState(
			MFBlocks.REDSTONE_CLOCK.get(),
			BlockRedstoneClock.POWERED,
			CubeModel() all blockId("redstone/redstone_clock_on"),
			CubeModel() all blockId("redstone/redstone_clock_off")
		)

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
		val builder = getMultipartBuilder(block.get())
		pipeConnection(builder, loc, Direction.UP, 180, 0)
		pipeConnection(builder, loc, Direction.DOWN, 0, 0)
		pipeConnection(builder, loc, Direction.NORTH, 90, 180)
		pipeConnection(builder, loc, Direction.EAST, 90, 270)
		pipeConnection(builder, loc, Direction.SOUTH, 90, 0)
		pipeConnection(builder, loc, Direction.WEST, 90, 90)
		builder.part().modelFile(pipeBaseModel(loc)).addModel().end()
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
