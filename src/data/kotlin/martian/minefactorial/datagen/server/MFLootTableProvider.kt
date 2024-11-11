package martian.minefactorial.datagen.server

import martian.dapper.api.server.DapperLootTableProvider
import martian.dapper.api.server.DapperLootTableProvider.Companion.BlockProvider
import martian.minefactorial.content.registry.MFBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.neoforge.data.event.GatherDataEvent

class MFLootTableProvider(event: GatherDataEvent) : DapperLootTableProvider(
	event,
	listOf(
		SubProviderEntry({ BlockLoot(it) }, LootContextParamSets.BLOCK)
	)
) {
	private class BlockLoot(registries: HolderLookup.Provider) : BlockProvider(registries) {
		override fun generate() {
			MFBlocks.REGISTRY.entries.forEach {
				if (it.get().lootTable === BuiltInLootTables.EMPTY) {
					return@forEach
				}
				dropSelf(it.get())
			}
		}
	}
}