package martian.minefactorial;

import com.mojang.logging.LogUtils;
import martian.minefactorial.client.MinefactorialClient;
import martian.minefactorial.content.block.BlockSteamBoilerBE;
import martian.minefactorial.content.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(Minefactorial.MODID)
public class Minefactorial {
	public static final String MODID = "minefactorial";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Minefactorial(IEventBus modBus, ModContainer modContainer) {
		MFFluids.REGISTRY.register(modBus);
		MFFluidTypes.REGISTRY.register(modBus);
		MFBlocks.REGISTRY.register(modBus);
		MFBlockEntityTypes.REGISTRY.register(modBus);
		MFItems.REGISTRY.register(modBus);
		MFTabs.REGISTRY.register(modBus);
		MFMenuTypes.REGISTRY.register(modBus);

		modBus.addListener((final RegisterCapabilitiesEvent event) -> {
			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MFBlockEntityTypes.STEAM_TURBINE.get(), (be, direction) -> be.getEnergyStorage());
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MFBlockEntityTypes.STEAM_TURBINE.get(), (be, direction) -> be.getFluidStorage());

			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MFBlockEntityTypes.STEAM_BOILER.get(), BlockSteamBoilerBE::getTankForSide);
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MFBlockEntityTypes.STEAM_BOILER.get(), (be, direction) -> be.getItemHandler());

			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MFBlockEntityTypes.CAPACITOR.get(), (be, direction) -> be.getEnergyStorage());
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MFBlockEntityTypes.CAPACITOR.get(), (be, direction) -> be.getItemHandler());

			event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MFBlockEntityTypes.BREAKER.get(), (be, direction) -> be.getEnergyStorage());
		});

		modBus.addListener(MFTabs::addItems);

		modBus.register(MinefactorialClient.class);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static ResourceLocation id(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
}
