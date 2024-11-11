package martian.minefactorial.datagen.server

import martian.minefactorial.content.registry.MFItems
import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern2x2
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.smeltsTo
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.unlockWith
import martian.minefactorial.datagen.id
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFRecipeProvider(event: GatherDataEvent) : DapperRecipeProvider(event) {
	override fun buildRecipes() {
		(MFItems.RAW_RUBBER smeltsTo MFItems.RUBBER_INGOT
			unlockWith MFItems.RAW_RUBBER
			save "smelting/rubber_ingot_from_raw_rubber".id)

		(MFItems.RUBBER_INGOT smeltsTo MFItems.RAW_PLASTIC
			unlockWith MFItems.RUBBER_INGOT
			save "smelting/raw_plastic_from_rubber_ingot".id)

		(MFItems.PLASTIC_SHEETS.shapedRecipeBuilder(count = 4)
			pattern2x2 MFItems.RAW_PLASTIC
			unlockWith MFItems.RAW_PLASTIC
			save "shaped/plastic_sheets".id)

		// An alternate method is:
		// MFItems.PLASTIC_SHEETS.shapedRecipeBuilder(count = 4).apply {
		// 	pattern2x2(MFItems.RAW_PLASTIC)
		// 	unlockWith(MFItems.RAW_PLASTIC)
		// 	save("shaped/plastic_sheets".id)
		// }
	}
}