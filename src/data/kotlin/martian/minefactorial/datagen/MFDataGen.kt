package martian.minefactorial.datagen

import martian.dapper.api.add
import martian.dapper.api.addProviders
import martian.minefactorial.Minefactorial
import martian.minefactorial.datagen.client.MFBlockStateProvider
import martian.minefactorial.datagen.client.MFItemModelProvider
import martian.minefactorial.datagen.server.MFBlockTagsProvider
import martian.minefactorial.datagen.server.MFItemTagsProvider
import martian.minefactorial.datagen.server.MFLootTableProvider
import martian.minefactorial.datagen.server.MFRecipeProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.MOD)
object MFDataGen {
	@JvmStatic
	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) {
		event.addProviders(
			client = {
				it.add(MFBlockStateProvider(event))
				it.add(MFItemModelProvider(event))
			},
			server = {
				it.add(MFLootTableProvider(event))
				it.add(MFBlockTagsProvider(event))
				it.add(MFItemTagsProvider(event))
				it.add(MFRecipeProvider(event))
			}
		)
	}
}

val String.id get() = Minefactorial.id(this)
