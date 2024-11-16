package martian.minefactorial.datagen.server

import martian.minefactorial.content.registry.MFItems
import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern2x2
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.shapelessRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.smeltsTo
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.unlockWith
import martian.minefactorial.content.MFTags
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.datagen.id
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFRecipeProvider(event: GatherDataEvent) : DapperRecipeProvider(event) {
	override fun buildRecipes() {
		/**
		 * Hey, you! Yes, you! If you are editing this file:
		 * USE REGIONS!!! Please help me keep this code clean!
		 * Data generation is never pretty, Dapper does help, but
		 * obviously it is not going to fix the sheer amount of
		 * repetition going on here. For that reason, **please** use
		 * region blocks to allow code folding and faster visual
		 * grepping.
		 * Kthxbai!!
		 *
		 * (for those who do not know, this is a region block)
		 * // region [some name]
		 * // endregion
		 * Within that area is collapsable by Intellij and also will
		 * allow future readers to find where they are in a long file.
		 *
		 * Another note: name your regions using this format, for
		 * example:
		 * // region Items
		 * // region Items/Tools
		 * // endregion Items/Tools
		 * // endregion Items
		 *
		 * Okay rant over
		 */


		// region Items

		// region Items/Tools
		MFItems.STRAW.shapedRecipeBuilder().apply {
			pattern("PP")
			pattern("P ")
			pattern("P ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/tools/straw".id)
		}

		MFItems.WRENCH.shapedRecipeBuilder().apply {
			pattern("I I")
			pattern(" P ")
			pattern(" I ")
			define('I', Tags.Items.INGOTS_IRON)
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/tools/wrench".id)
		}

		MFItems.TREE_TAP.shapedRecipeBuilder().apply {
			pattern(" S ")
			pattern("PPP")
			pattern("  P")
			define('S', Items.STICK)
			define('P', ItemTags.PLANKS)
			unlockWith(Items.STICK)
			save("shaped/tools/tree_tap".id)
		}
		// endregion Items/Tools

		// region Items/Resources
		(MFItems.RAW_RUBBER smeltsTo MFItems.RUBBER_INGOT
			unlockWith MFItems.RAW_RUBBER
			save "smelting/resources/rubber_ingot_from_raw_rubber".id)

		(MFItems.RUBBER_INGOT smeltsTo MFItems.RAW_PLASTIC
			unlockWith MFItems.RUBBER_INGOT
			save "smelting/resources/raw_plastic_from_rubber_ingot".id)

		MFItems.PLASTIC_SHEETS.shapedRecipeBuilder(4).apply {
			pattern2x2(MFItems.RAW_PLASTIC)
			unlockWith(MFItems.RAW_PLASTIC)
			save("shaped/resources/plastic_sheets".id)
		}
		//endregion Items/Resources

		// endregion Items

		// region Blocks

		// region Blocks/Logistics
		MFBlocks.CONVEYOR.shapedRecipeBuilder(16).apply {
			pattern("RRR")
			pattern("DID")
			define('R', MFTags.Items.RUBBER_INGOT)
			define('D', Tags.Items.DUSTS_REDSTONE)
			define('I', Tags.Items.INGOTS_IRON)
			unlockWith(MFItems.RUBBER_INGOT)
			save("shaped/logistics/conveyor".id)
		}

		MFBlocks.EJECTOR.shapedRecipeBuilder(4).apply {
			pattern("PPP")
			pattern(" D ")
			pattern("RFR")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('D', Items.DROPPER)
			define('R', Tags.Items.DUSTS_REDSTONE)
			define('F', MFBlocks.MACHINE_FRAME)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/logistics/ejector".id)
		}

		MFBlocks.FLUID_EXTRACTOR.shapedRecipeBuilder(2).apply {
			pattern("P P")
			pattern("RBR")
			pattern("P P")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('B', Items.BUCKET)
			define('R', Tags.Items.DUSTS_REDSTONE)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/logistics/fluid_extractor".id)
		}

		MFBlocks.ENERGY_PIPE.shapedRecipeBuilder(8).apply {
			pattern("PPP")
			pattern(" R ")
			pattern("PPP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('R', Tags.Items.DUSTS_REDSTONE)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/logistics/energy_pipe".id)
		}

		MFBlocks.FLUID_PIPE.shapedRecipeBuilder(8).apply {
			pattern("PPP")
			pattern(" B ")
			pattern("PPP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('B', Items.BUCKET)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/logistics/fluid_pipe".id)
		}
		// endregion Blocks/Logistics

		// region Blocks/Machinery
		MFBlocks.BREAKER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("IHS")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('I', Items.GOLDEN_PICKAXE)
			define('S', Items.GOLDEN_SHOVEL)
			define('H', MFItems.WRENCH)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/breaker".id)
		}

		MFBlocks.MOB_GRINDER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("BSB")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('S', Items.GOLDEN_SWORD)
			define('B', Items.BOOK)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/mob_grinder".id)
		}

		MFBlocks.FOUNTAIN.shapedRecipeBuilder().apply {
			pattern("PBP")
			pattern("PBP")
			pattern("UFU")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('U', MFBlocks.FLUID_PIPE)
			define('B', Items.IRON_BARS)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/fountain".id)
		}

		MFBlocks.PUMP.shapedRecipeBuilder().apply {
			pattern("PBP")
			pattern(" U ")
			pattern("RFR")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('B', Items.IRON_BARS)
			define('U', Items.BUCKET)
			define('R', Tags.Items.DUSTS_REDSTONE)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/pump".id)
		}

		MFBlocks.PLACER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("BHB")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('B', Items.BRICKS)
			define('H', MFItems.WRENCH)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/placer".id)
		}
		// endregion Blocks/Machinery

		// region Blocks/Power
		MFBlocks.STEAM_BOILER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("TFT")
			pattern("NNN")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('N', Items.NETHER_BRICKS)
			define('T', MFBlocks.PLASTIC_TANK)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/power/steam_boiler".id)
		}

		MFBlocks.STEAM_TURBINE.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("ITI")
			pattern("NFN")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('N', Items.NETHER_BRICKS)
			define('T', MFBlocks.PLASTIC_TANK)
			define('I', Items.PISTON)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/power/steam_turbine".id)
		}
		// endregion Blocks/Power

		// region Blocks/Redstone
		MFBlocks.REDSTONE_CLOCK.shapedRecipeBuilder(4).apply {
			pattern("P P")
			pattern(" R ")
			pattern("P P")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/redstone/redstone_clock".id)
		}
		// endregion Blocks/Redstone

		// region Blocks/Storage
		MFBlocks.CAPACITOR.shapedRecipeBuilder().apply {
			pattern("PCP")
			pattern("PRP")
			pattern("PCP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('C', MFBlocks.ENERGY_PIPE)
			define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			unlockWith(MFBlocks.ENERGY_PIPE)
			save("shaped/storage/capacitor".id)
		}

		MFBlocks.PLASTIC_TANK.shapedRecipeBuilder().apply {
			pattern("PCP")
			pattern("PBP")
			pattern("PCP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('C', MFBlocks.FLUID_PIPE)
			define('B', Items.BUCKET)
			unlockWith(MFBlocks.FLUID_PIPE)
			save("shaped/storage/plastic_tank".id)
		}
		// endregion Blocks/Storage

		// region Blocks/Misc
		MFBlocks.MACHINE_FRAME.shapedRecipeBuilder(3).apply {
			pattern("PPP")
			pattern("SSS")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('S', Tags.Items.STONES)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/misc/machine_frame".id)
		}
		// endregion Blocks/Misc

		// endregion Blocks

		// region Misc/Wood Products
		Items.JUNGLE_PLANKS.shapelessRecipeBuilder(4).apply {
			requires(MFBlocks.RUBBER_WOOD)
			unlockWith(MFBlocks.RUBBER_WOOD)
			save("shapeless/woodworks/rubber_wood_to_jungle_planks".id)
		}
		// endregion Misc/Wood Products
	}
}