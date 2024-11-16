package martian.minefactorial.datagen.client

import martian.dapper.api.client.DapperItemModelProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.content.registry.MFFluids
import martian.minefactorial.content.registry.MFItems
import martian.minefactorial.datagen.id
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFItemModelProvider(event: GatherDataEvent) : DapperItemModelProvider(event, Minefactorial.MODID) {
	override fun registerModels() {
		// Tools
		MFItems.STRAW.basicHandheldModel()
		MFItems.MEGA_STRAW.basicHandheldModel()
		MFItems.WRENCH.basicHandheldModel()
		MFItems.TREE_TAP.basicHandheldModel()

		// Resources
		MFItems.RAW_RUBBER.basicModel()
		MFItems.RUBBER_INGOT.basicModel()
		MFItems.RAW_PLASTIC.basicModel()
		MFItems.PLASTIC_INGOT.basicModel()
		MFItems.PLASTIC_SHEETS.basicModel()

		// Buckets
		MFFluids.STEAM_BUCKET.basicModel()
		MFFluids.OIL_BUCKET.basicModel()
		MFFluids.ESSENCE_BUCKET.basicModel()

		// Block items with models that are not just parenting its block model
		basicItem("conveyor".id)
		MFBlocks.RUBBER_SAPLING.asItem() addModel "block/foliage/rubber_sapling".id
	}
}