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
	}
}