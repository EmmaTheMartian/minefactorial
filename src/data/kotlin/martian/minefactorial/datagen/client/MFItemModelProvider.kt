package martian.minefactorial.datagen.client

import martian.dapper.api.client.DapperItemModelProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFFluids
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFItemModelProvider(event: GatherDataEvent) : DapperItemModelProvider(event, Minefactorial.MODID) {
	override fun registerModels() {
		MFFluids.STEAM_BUCKET.basicModel()
		MFFluids.OIL_BUCKET.basicModel()
		MFFluids.ESSENCE_BUCKET.basicModel()
	}
}