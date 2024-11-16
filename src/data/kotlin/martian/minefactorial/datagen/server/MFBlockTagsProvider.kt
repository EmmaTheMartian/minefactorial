package martian.minefactorial.datagen.server

import martian.dapper.api.server.DapperTagProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFBlockTagsProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Blocks(event, Minefactorial.MODID) {
	override fun addTags() {
		// Wooden things
		MFBlocks.RUBBER_WOOD addTo mcTag("logs")
		MFBlocks.RUBBER_LEAVES addTo mcTag("leaves")
		MFBlocks.RUBBER_SAPLING addTo mcTag("saplings")

		// Mineable with Stone-tier minimum
		addToMultipleTags(
			setOf(mcTag("mineable/pickaxe"), mcTag("needs_stone_tool")),
			MFBlocks.CONVEYOR,
			MFBlocks.EJECTOR,
			MFBlocks.FLUID_EXTRACTOR,
			MFBlocks.ENERGY_PIPE,
			MFBlocks.FLUID_PIPE,
			MFBlocks.BREAKER,
			MFBlocks.MOB_GRINDER,
			MFBlocks.FOUNTAIN,
			MFBlocks.PUMP,
			MFBlocks.PLACER,
			MFBlocks.STEAM_BOILER,
			MFBlocks.STEAM_TURBINE,
			MFBlocks.REDSTONE_CLOCK,
			MFBlocks.CAPACITOR,
			MFBlocks.CREATIVE_CAPACITOR,
			MFBlocks.PLASTIC_TANK,
			MFBlocks.CREATIVE_TANK,
			MFBlocks.STORAGE_UNIT,
			MFBlocks.DECOR_BLOCKS.get("plastic_block"),
			MFBlocks.DECOR_BLOCKS.get("smooth_plastic"),
			MFBlocks.DECOR_BLOCKS.get("plastic_bricks"),
			MFBlocks.DECOR_BLOCKS.get("plastic_pillar"),
			MFBlocks.ROAD_BLOCKS.get("plastic_road"),
		)
	}
}