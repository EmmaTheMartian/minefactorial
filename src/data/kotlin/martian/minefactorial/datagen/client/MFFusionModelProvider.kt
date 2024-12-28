package martian.minefactorial.datagen.client

import com.supermartijn642.fusion.api.model.DefaultModelTypes
import com.supermartijn642.fusion.api.model.ModelInstance
import com.supermartijn642.fusion.api.model.data.ConnectingModelDataBuilder
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates
import com.supermartijn642.fusion.api.provider.FusionModelProvider
import martian.dapper.api.mcId
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.datagen.id
import net.minecraft.data.PackOutput

class MFFusionModelProvider(packOutput: PackOutput) : FusionModelProvider(Minefactorial.MODID, packOutput) {
    override fun generate() {
        MFBlocks.ROAD_BLOCKS.entries.forEach {
            val modelData = ConnectingModelDataBuilder.builder()
                .parent("block/cube_all".mcId)
                .texture("all", "block/decor/roads/${it.key}".id)
                .connection(DefaultConnectionPredicates.isSameBlock())
                .build()
            val modelInstance = ModelInstance.of(DefaultModelTypes.CONNECTING, modelData)
            addModel("block/${it.key}".id, modelInstance)
        }
    }
}