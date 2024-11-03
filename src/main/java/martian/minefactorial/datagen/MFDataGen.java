package martian.minefactorial.datagen;

import martian.minefactorial.datagen.client.MFBlockStateProvider;
import martian.minefactorial.datagen.client.MFItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class MFDataGen {
	private MFDataGen() { }

	public static void onGatherData(final GatherDataEvent event) {
		if (event.includeClient()) {
			runClientGenerators(event);
		}

		if (event.includeServer()) {
			runServerGenerators(event);
		}
	}

	private static void runClientGenerators(final GatherDataEvent event) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		ExistingFileHelper efh = event.getExistingFileHelper();

		dataGenerator.addProvider(true, new MFBlockStateProvider(packOutput, efh));
		dataGenerator.addProvider(true, new MFItemModelProvider(packOutput, efh));
	}

	private static void runServerGenerators(final GatherDataEvent event) {
	}
}
