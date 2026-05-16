package martian.minefactorial.datagen

import martian.dapper.api.add
import martian.dapper.api.addProviders
import martian.minefactorial.Minefactorial
import martian.minefactorial.datagen.client.MFBlockStateProvider
import martian.minefactorial.datagen.client.MFFusionMetadataProvider
import martian.minefactorial.datagen.client.MFFusionModelProvider
import martian.minefactorial.datagen.client.MFItemModelProvider
import martian.minefactorial.datagen.server.*
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Minefactorial.MODID + "_data")
object MFDataGen {
	init {
		MOD_BUS.addListener(::onGatherData)
	}

	fun onGatherData(event: GatherDataEvent) {
		event.addProviders(
			client = {
				it.add(MFBlockStateProvider(event))
				it.add(MFItemModelProvider(event))

				val fusionPackOutput = PackOutput(event.generator.packOutput.outputFolder.resolve("fusion"))
				println(fusionPackOutput.outputFolder.toString())
				it.add(MFFusionModelProvider(fusionPackOutput))
				it.add(MFFusionMetadataProvider(fusionPackOutput))
			},
			server = {
				it.add(MFLootTableProvider(event))
				it.add(MFBlockTagsProvider(event))
				it.add(MFFluidTagsProvider(event))
				it.add(MFItemTagsProvider(event))
				it.add(MFRecipeProvider(event))
			}
		)
	}
}

val String.id: ResourceLocation get() = Minefactorial.id(this)
