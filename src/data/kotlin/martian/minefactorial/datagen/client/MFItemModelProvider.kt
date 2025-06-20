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
		MFItems.SCREWDRIVER.basicHandheldModel()
		MFItems.RULER.basicModel()
		MFItems.TWEAKERULER.basicModel()
		MFItems.SAFARI_NET.basicModel()

		// Resources
		MFItems.RAW_RUBBER.basicModel()
		MFItems.RUBBER_INGOT.basicModel()
		MFItems.RAW_PLASTIC.basicModel()
		MFItems.PLASTIC_INGOT.basicModel()
		MFItems.PLASTIC_SHEETS.basicModel()
		MFItems.RAW_MEAT_INGOT.basicModel()
		MFItems.COOKED_MEAT_INGOT.basicModel()

		MFItems.ORE_DUSTS.get("coal_dust").basicModel()
		MFItems.ORE_DUSTS.get("iron_dust").basicModel()
		MFItems.ORE_DUSTS.get("copper_dust").basicModel()
		MFItems.ORE_DUSTS.get("gold_dust").basicModel()
		MFItems.ORE_DUSTS.get("diamond_dust").basicModel()
		MFItems.ORE_DUSTS.get("ancient_debris_dust").basicModel()

		MFItems.MACERATED_ORES.get("macerated_coal").basicModel()
		MFItems.MACERATED_ORES.get("macerated_iron").basicModel()
		MFItems.MACERATED_ORES.get("macerated_copper").basicModel()
		MFItems.MACERATED_ORES.get("macerated_gold").basicModel()
		MFItems.MACERATED_ORES.get("macerated_diamond").basicModel()
		MFItems.MACERATED_ORES.get("macerated_ancient_debris").basicModel()

		// Buckets
		MFFluids.STEAM_BUCKET.basicModel()
		MFFluids.OIL_BUCKET.basicModel()
		MFFluids.ESSENCE_BUCKET.basicModel()
		MFFluids.BEETROOT_SOUP_BUCKET.basicModel()
		MFFluids.MUSHROOM_STEW_BUCKET.basicModel()
		MFFluids.SUSPICIOUS_STEW_BUCKET.basicModel()
		MFFluids.RABBIT_STEW_BUCKET.basicModel()
		MFFluids.HONEY_BUCKET.basicModel()
		MFFluids.PINK_SLIME_BUCKET.basicModel()
		MFFluids.MEAT_BUCKET.basicModel()
		MFFluids.INDUSTRIAL_FERTILIZER_BUCKET.basicModel()

		// Block items with models that are not just parenting its block model
		basicItem("conveyor".id)
		basicItem("hopping_conveyor".id)
		MFBlocks.RUBBER_SAPLING.asItem() addModel "block/foliage/rubber_sapling".id
	}
}