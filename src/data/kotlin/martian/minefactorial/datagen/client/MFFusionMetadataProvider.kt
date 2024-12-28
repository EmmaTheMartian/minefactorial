package martian.minefactorial.datagen.client

import com.supermartijn642.fusion.api.provider.FusionTextureMetadataProvider
import com.supermartijn642.fusion.api.texture.DefaultTextureTypes
import com.supermartijn642.fusion.api.texture.data.ConnectingTextureData
import com.supermartijn642.fusion.api.texture.data.ConnectingTextureLayout
import martian.minefactorial.Minefactorial
import martian.minefactorial.content.registry.MFBlocks
import martian.minefactorial.datagen.id
import net.minecraft.data.PackOutput

class MFFusionMetadataProvider(packOutput: PackOutput) : FusionTextureMetadataProvider(Minefactorial.MODID, packOutput) {
    override fun generate() {
        MFBlocks.ROAD_BLOCKS.entries.forEach {
            val textureData = ConnectingTextureData.builder()
                .layout(ConnectingTextureLayout.SIMPLE)
                .build()
            addTextureMetadata("block/decor/roads/${it.key}".id, DefaultTextureTypes.CONNECTING, textureData)
        }
    }
}