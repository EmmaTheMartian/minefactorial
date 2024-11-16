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
			val skippedBlocks = setOf(MFBlocks.RUBBER_LEAVES)
			MFBlocks.REGISTRY.entries.forEach {
				if (it.get().lootTable === BuiltInLootTables.EMPTY || skippedBlocks.contains(it)) {
					return@forEach
				}
				dropSelf(it.get())
			}
		}
	}
}