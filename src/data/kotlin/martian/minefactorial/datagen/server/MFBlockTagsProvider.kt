package martian.minefactorial.datagen.server

import martian.dapper.api.server.DapperTagProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks.*
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFBlockTagsProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Blocks(event, Minefactorial.MODID) {
	override fun addTags() {
		// Wooden things
		RUBBER_WOOD addTo mcTag("logs")
		RUBBER_LEAVES addTo mcTag("leaves")
		RUBBER_SAPLING addTo mcTag("saplings")

		// Mineable with Stone-tier minimum
		val pickaxeMineable = mcTag("mineable/pickaxe")
		val needsStoneTool = mcTag("needs_stone_tool")
		addToMultipleTags(
			setOf(pickaxeMineable, needsStoneTool),
			// Logistics
			CONVEYOR,
			HOPPING_CONVEYOR,
			EJECTOR,
			FLUID_EXTRACTOR,
			ENERGY_PIPE,
			FLUID_PIPE,
			ITEM_ROUTER,
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
			RANCHER,
			FERTILIZER,
			SEWAGE_COLLECTOR,
			BREEDER,
			MOB_ROUTER,
			CHRONOTYPER,
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
		)
		DECOR_BLOCKS.entries.forEach {
			it.value addTo pickaxeMineable
			it.value addTo needsStoneTool
		}
		ROAD_BLOCKS.entries.forEach {
			it.value addTo pickaxeMineable
			it.value addTo needsStoneTool
		}
	}
}