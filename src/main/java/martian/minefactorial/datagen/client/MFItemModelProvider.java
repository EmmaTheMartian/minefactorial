package martian.minefactorial.datagen.client;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFFluids;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MFItemModelProvider extends ItemModelProvider {
	public MFItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Minefactorial.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		basicItem(MFFluids.STEAM_BUCKET.get());
		basicItem(MFFluids.OIL_BUCKET.get());
		basicItem(MFFluids.ESSENCE_BUCKET.get());
	}
}
