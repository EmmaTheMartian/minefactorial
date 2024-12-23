package martian.minefactorial.datagen.server

import martian.dapper.api.server.DapperTagProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.content.registry.MFBlocks.*
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFBlockTagsProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Blocks(event, Minefactorial.MODID) {
	override fun addTags() {
		// Wooden things
		RUBBER_WOOD addTo mcTag("logs")
		RUBBER_LEAVES addTo mcTag("leaves")
		RUBBER_SAPLING addTo mcTag("saplings")

		// Mineable with Stone-tier minimum
		addToMultipleTags(
			setOf(mcTag("mineable/pickaxe"), mcTag("needs_stone_tool")),
			// Logistics
			CONVEYOR,
			HOPPING_CONVEYOR,
			EJECTOR,
			FLUID_EXTRACTOR,
			ENERGY_PIPE,
			FLUID_PIPE,
			// Machinery
			BREAKER,
			MOB_GRINDER,
			FOUNTAIN,
			PUMP,
			PLACER,
			SMASHER,
			MACERATOR,
			PLANTER,
			HARVESTER,
			SLAUGHTERHOUSE,
			MEAT_PACKER,
			// Power
			STEAM_BOILER,
			STEAM_TURBINE,
			// Redstone
			REDSTONE_CLOCK,
			// Storage
			CAPACITOR,
			CREATIVE_CAPACITOR,
			PLASTIC_TANK,
			CREATIVE_TANK,
			STORAGE_UNIT,
			// Misc
			MACHINE_FRAME,
			// Bulk
			DECOR_BLOCKS.get("plastic_block"),
			DECOR_BLOCKS.get("smooth_plastic"),
			DECOR_BLOCKS.get("plastic_bricks"),
			DECOR_BLOCKS.get("plastic_pillar"),
			ROAD_BLOCKS.get("plastic_road"),
		)
	}
}