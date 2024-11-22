package martian.minefactorial.datagen.server

import martian.dapper.api.server.DapperTagProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFItems
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFItemTagsProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Items(event, Minefactorial.MODID) {
	override fun addTags() {
		MFItems.WRENCH addTo "c:tools/wrenches"
		MFItems.PLASTIC_INGOT addTo "c:ingots/plastic"
		MFItems.PLASTIC_SHEETS addTo "c:plates/plastic"
		MFItems.RUBBER_INGOT addTo "c:ingots/rubber"

		MFItems.ORE_DUSTS.get("coal_dust").addTo("c:dusts", "c:dusts/coal")
		MFItems.ORE_DUSTS.get("iron_dust").addTo("c:dusts", "c:dusts/iron")
		MFItems.ORE_DUSTS.get("copper_dust").addTo("c:dusts", "c:dusts/copper")
		MFItems.ORE_DUSTS.get("gold_dust").addTo("c:dusts", "c:dusts/gold")
		MFItems.ORE_DUSTS.get("diamond_dust").addTo("c:dusts", "c:dusts/diamond")
		MFItems.ORE_DUSTS.get("ancient_debris_dust").addTo("c:dusts", "c:dusts/netherite_scrap")

		MFItems.MACERATED_ORES.get("macerated_coal").addTo("c:macerated_ores", "c:macerated_ores/coal")
		MFItems.MACERATED_ORES.get("macerated_iron").addTo("c:macerated_ores", "c:macerated_ores/iron")
		MFItems.MACERATED_ORES.get("macerated_copper").addTo("c:macerated_ores", "c:macerated_ores/copper")
		MFItems.MACERATED_ORES.get("macerated_gold").addTo("c:macerated_ores", "c:macerated_ores/gold")
		MFItems.MACERATED_ORES.get("macerated_diamond").addTo("c:macerated_ores", "c:macerated_ores/diamond")
		MFItems.MACERATED_ORES.get("macerated_ancient_debris").addTo("c:macerated_ores", "c:macerated_ores/netherite_scrap")
	}
}