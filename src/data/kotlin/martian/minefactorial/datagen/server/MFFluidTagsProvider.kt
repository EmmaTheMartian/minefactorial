package martian.minefactorial.datagen.server

import martian.dapper.api.server.tag.DapperTagProvider
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.MFTags
import martian.minefactorial.content.registry.MFFluids
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFFluidTagsProvider(event: GatherDataEvent) : DapperTagProvider.Companion.Fluids(event, Minefactorial.MODID) {
	override fun addTags() {
		MFFluids.STEAM.addTo("c:steam", "c:gaseous")
		MFFluids.OIL addTo "c:oil"
		MFFluids.ESSENCE addTo "c:essence"
		MFFluids.BEETROOT_SOUP addTo "c:beetroot_soup"
		MFFluids.MUSHROOM_STEW addTo "c:mushroom_stew"
		MFFluids.SUSPICIOUS_STEW addTo "c:suspicious_stew"
		MFFluids.RABBIT_STEW addTo "c:rabbit_stew"
		MFFluids.HONEY addTo "c:honey"
		MFFluids.INDUSTRIAL_FERTILIZER addTo tag(MFTags.Fluids.FERTILIZERS)
	}
}