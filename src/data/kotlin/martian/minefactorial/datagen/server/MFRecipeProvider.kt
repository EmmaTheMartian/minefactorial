package martian.minefactorial.datagen.server

import martian.dapper.api.ofStack
import martian.dapper.api.server.recipe.DapperRecipeProvider
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.pattern2x2
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.shapedRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapedRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.shapelessRecipeBuilder
import martian.dapper.api.server.recipe.DapperShapelessRecipeUtil.unlockWith
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.smeltsTo
import martian.dapper.api.server.recipe.DapperSmeltingRecipeUtil.unlockWith
import martian.minefactorial.content.MFTags
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.content.registry.MFFluids
import martian.minefactorial.content.registry.MFItems
import martian.minefactorial.datagen.id
import martian.minefactorial.datagen.server.recipe.RecipeBuilderMaceration
import martian.minefactorial.datagen.server.recipe.RecipeBuilderMeatPacking
import martian.minefactorial.datagen.server.recipe.RecipeBuilderRanching
import martian.minefactorial.foundation.entity.EntityIngredient
import martian.minefactorial.foundation.fluid.ChancedFluidStack
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

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

		MFItems.SCREWDRIVER.shapedRecipeBuilder().apply {
			pattern("I")
			pattern("P")
			pattern("I")
			define('I', Tags.Items.INGOTS_IRON)
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/tools/screwdriver".id)
		}

		MFItems.RULER.shapedRecipeBuilder().apply {
			pattern("PPP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/tools/ruler".id)
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

		MFItems.SAFARI_NET.shapedRecipeBuilder().apply {
			pattern("IPI")
			pattern("PSP")
			pattern("IPI")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('I', Tags.Items.NUGGETS_IRON)
			define('S', Tags.Items.SLIME_BALLS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/tools/safari_net".id)
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

		MFItems.RAW_MEAT_INGOT.shapelessRecipeBuilder(4).apply {
			requires(MFFluids.MEAT_BUCKET)
			unlockWith(MFFluids.MEAT_BUCKET)
			save("shapeless/resources/raw_meat_ingot".id)
		}

		RecipeBuilderMeatPacking(FluidIngredient.single(MFFluids.MEAT), MFItems.RAW_MEAT_INGOT ofStack 1)
			.save("meat_packing/resources/raw_meat_ingot".id)

		(MFItems.RAW_MEAT_INGOT smeltsTo MFItems.COOKED_MEAT_INGOT
			unlockWith MFItems.RAW_MEAT_INGOT
			save "smelting/resources/cooked_meat_from_raw_meat".id)
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

		MFBlocks.HOPPING_CONVEYOR.shapelessRecipeBuilder().apply {
			requires(MFBlocks.CONVEYOR)
			requires(Items.HOPPER)
			unlockWith(MFBlocks.CONVEYOR)
			save("shapeless/logistics/hopping_conveyor".id)
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

		MFBlocks.ITEM_ROUTER.shapedRecipeBuilder().apply {
			pattern("PCP")
			pattern("CFC")
			pattern("PCP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('C', MFBlocks.CONVEYOR)
			define('F', MFBlocks.MACHINE_FRAME)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/logistics/item_router".id)
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

		MFBlocks.SMASHER.shapedRecipeBuilder().apply {
			pattern("PHP")
			pattern("BIB")
			pattern("DFD")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('B', Items.BOOK)
			define('D', Items.DIAMOND)
			define('H', Items.PISTON)
			define('I', Items.IRON_BLOCK)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/smasher".id)
		}

		MFBlocks.MACERATOR.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("DID")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('D', Tags.Items.GEMS_DIAMOND)
			define('I', Items.PISTON)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/macerator".id)
		}

		MFBlocks.PLANTER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("IHI")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('H', Items.GOLDEN_HOE)
			define('I', Tags.Items.INGOTS_IRON)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/planter".id)
		}

		MFBlocks.HARVESTER.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("ISI")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('S', Tags.Items.SEEDS)
			define('I', Tags.Items.INGOTS_IRON)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/harvester".id)
		}

		MFBlocks.SLAUGHTERHOUSE.shapedRecipeBuilder().apply {
			pattern("PPP")
			pattern("TST")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('S', Items.GOLDEN_SWORD)
			define('T', MFBlocks.PLASTIC_TANK)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/slaughterhouse".id)
		}

		MFBlocks.MEAT_PACKER.shapedRecipeBuilder().apply {
			pattern("PHP")
			pattern("PIP")
			pattern(" F ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('F', MFBlocks.MACHINE_FRAME)
			define('H', Items.PISTON)
			define('I', Items.CAULDRON)
			unlockWith(MFBlocks.MACHINE_FRAME)
			save("shaped/machinery/meat_packer".id)
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
			pattern(" P ")
			pattern("PCP")
			pattern(" P ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			define('C', Items.CLOCK)
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

		// region Blocks/Decor
		MFBlocks.DECOR_BLOCKS.get("plastic_block").shapedRecipeBuilder(4).apply {
			pattern("PP")
			pattern("PP")
			define('P', MFItems.RAW_PLASTIC)
			unlockWith(MFItems.RAW_PLASTIC)
			save("shaped/decor/plastic_block".id)
		}

		MFBlocks.DECOR_BLOCKS.get("smooth_plastic").shapedRecipeBuilder(4).apply {
			pattern("PP")
			pattern("PP")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/decor/smooth_plastic".id)
		}

		MFBlocks.DECOR_BLOCKS.get("plastic_bricks").shapedRecipeBuilder(4).apply {
			pattern("PP")
			pattern("PP")
			define('P', MFBlocks.DECOR_BLOCKS.get("smooth_plastic"))
			unlockWith(MFBlocks.DECOR_BLOCKS.get("smooth_plastic"))
			save("shaped/decor/plastic_bricks".id)
		}

		MFBlocks.DECOR_BLOCKS.get("plastic_pillar").shapedRecipeBuilder(2).apply {
			pattern("P")
			pattern("P")
			define('P', MFBlocks.DECOR_BLOCKS.get("smooth_plastic"))
			unlockWith(MFBlocks.DECOR_BLOCKS.get("smooth_plastic"))
			save("shaped/decor/plastic_pillar".id)
		}
		// endregion Blocks/Decor

		// region Blocks/Roads
		MFBlocks.ROAD_BLOCKS.get("plastic_road").shapedRecipeBuilder(4).apply {
			pattern(" P ")
			pattern("P P")
			pattern(" P ")
			define('P', MFTags.Items.PLASTIC_SHEETS)
			unlockWith(MFItems.PLASTIC_SHEETS)
			save("shaped/roads/plastic_road".id)
		}

		fun concreteRoadRecipe(result: ItemLike, centre: ItemLike, id: String) {
			result.shapedRecipeBuilder(4).apply {
				pattern(" P ")
				pattern("PCP")
				pattern(" P ")
				define('P', MFTags.Items.PLASTIC_SHEETS)
				define('C', centre)
				unlockWith(MFItems.PLASTIC_SHEETS)
				save(id.id)
			}
		}
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("white_concrete_road"), Items.WHITE_CONCRETE, "shaped/roads/white_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("orange_concrete_road"), Items.ORANGE_CONCRETE, "shaped/roads/orange_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("magenta_concrete_road"), Items.MAGENTA_CONCRETE, "shaped/roads/magenta_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("light_blue_concrete_road"), Items.LIGHT_BLUE_CONCRETE, "shaped/roads/light_blue_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("yellow_concrete_road"), Items.YELLOW_CONCRETE, "shaped/roads/yellow_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("lime_concrete_road"), Items.LIME_CONCRETE, "shaped/roads/lime_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("pink_concrete_road"), Items.PINK_CONCRETE, "shaped/roads/pink_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("gray_concrete_road"), Items.GRAY_CONCRETE, "shaped/roads/gray_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("light_gray_concrete_road"), Items.LIGHT_GRAY_CONCRETE, "shaped/roads/light_gray_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("cyan_concrete_road"), Items.CYAN_CONCRETE, "shaped/roads/cyan_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("purple_concrete_road"), Items.PURPLE_CONCRETE, "shaped/roads/purple_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("blue_concrete_road"), Items.BLUE_CONCRETE, "shaped/roads/blue_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("brown_concrete_road"), Items.BROWN_CONCRETE, "shaped/roads/brown_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("green_concrete_road"), Items.GREEN_CONCRETE, "shaped/roads/green_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("red_concrete_road"), Items.RED_CONCRETE, "shaped/roads/red_concrete_road")
		concreteRoadRecipe(MFBlocks.ROAD_BLOCKS.get("black_concrete_road"), Items.BLACK_CONCRETE, "shaped/roads/black_concrete_road")
		// endregion Blocks/Roads

		// endregion Blocks

		// region Misc

		// region Misc/Wood Products
		Items.JUNGLE_PLANKS.shapelessRecipeBuilder(4).apply {
			requires(MFBlocks.RUBBER_WOOD)
			unlockWith(MFBlocks.RUBBER_WOOD)
			save("shapeless/woodworks/rubber_wood_to_jungle_planks".id)
		}
		// endregion Misc/Wood

		// region Misc/Ore Processing
		fun oreProcessingChain(id: String, result: ItemLike?, ore: Ingredient, raw: Ingredient? = null, byproductId: String? = null) {
			val dust = MFItems.ORE_DUSTS.get("${id}_dust")
			val macerated = MFItems.MACERATED_ORES.get("macerated_${id}")
			val byproductDust = if (byproductId != null) {
				MFItems.ORE_DUSTS.get("${byproductId}_dust")
			} else { null }
			val byproductMacerated = if (byproductId != null) {
				MFItems.MACERATED_ORES.get("macerated_${byproductId}")
			} else { null }

			// macerated and dust -> result
			if (result != null) {
				macerated smeltsTo result unlockWith macerated save "smelting/macerated_ores/${id}".id
				dust smeltsTo result unlockWith dust save "smelting/dusts/${id}".id
			}

			RecipeBuilderMaceration(
				ore,
				2,
				// If there is not a raw ore then we should make the ore->macerated recipe a bit nicer
				if (raw == null) 60 else 120
			).apply {
				if (raw == null && byproductMacerated != null) {
					addResult(byproductMacerated, 0.25F)
				}
				addResult(macerated)
				save("maceration/ores/${id}".id)
			}

			// raw -> macerated + byproducts
			if (raw != null) {
				RecipeBuilderMaceration(raw, 2, 60).apply {
					if (byproductMacerated != null) {
						addResult(byproductMacerated, 0.25F)
					}
					addResult(macerated)
					save("maceration/raw_materials/${id}".id)
				}
			}

//			ore.centrifugeRecipeBuilder() result result
		}

		oreProcessingChain("coal",
			Items.COAL,
			Ingredient.of(Tags.Items.ORES_COAL))
		oreProcessingChain("iron",
			Items.IRON_INGOT,
			Ingredient.of(Tags.Items.ORES_IRON),
			raw = Ingredient.of(Tags.Items.RAW_MATERIALS_IRON))
		oreProcessingChain("copper",
			Items.COPPER_INGOT,
			Ingredient.of(Tags.Items.ORES_COPPER),
			raw = Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER),
			byproductId = "gold")
		oreProcessingChain("gold",
			Items.GOLD_INGOT,
			Ingredient.of(Tags.Items.ORES_GOLD),
			raw = Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD),
			byproductId = "copper")
		oreProcessingChain("diamond",
			Items.DIAMOND,
			Ingredient.of(Tags.Items.ORES_DIAMOND),
			byproductId = "coal")
		oreProcessingChain("ancient_debris",
			Items.NETHERITE_SCRAP,
			Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP))
		// endregion Misc/Ore Processing

		// region Misc/Ranching
		RecipeBuilderRanching(EntityIngredient.of(EntityType.COW)).apply {
			setResultFluid(ChancedFluidStack(NeoForgeMod.MILK.get(), 250, 0.1f))
			save("ranching/cow_to_milk.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.CHICKEN)).apply {
			setResultItem(Items.FEATHER, 0.5f)
			save("ranching/chicken_to_feather.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.PARROT)).apply {
			setResultItem(Items.FEATHER, 0.5f)
			save("ranching/parrot_to_feather.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.SQUID)).apply {
			setResultItem(Items.INK_SAC, 0.5f)
			save("ranching/squid_to_ink_sac.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.GLOW_SQUID)).apply {
			setResultItem(Items.GLOW_INK_SAC, 0.5f)
			save("ranching/glow_squid_to_glow_inc_sac.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.SPIDER)).apply {
			setResultItem(Items.STRING, 0.25f)
			save("ranching/spider_to_string.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.CREEPER)).apply {
			setResultItem(Items.GUNPOWDER, 0.25f)
			save("ranching/creeper_to_gunpowder.json".id)
		}

		RecipeBuilderRanching(EntityIngredient.of(EntityType.SLIME)).apply {
			setResultItem(Items.SLIME_BALL, 0.25f)
			save("ranching/slime_to_slime_ball.json".id)
		}
		// endregion Misc/Ranching

		// endregion Misc
	}
}