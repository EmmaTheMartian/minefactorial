package martian.minefactorial.client;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.client.screen.ScreenBreaker;
import martian.minefactorial.client.screen.ScreenCapacitor;
import martian.minefactorial.client.screen.ScreenSteamBoiler;
import martian.minefactorial.client.screen.ScreenSteamTurbine;
import martian.minefactorial.foundation.fluid.BasicFluidType;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.content.registry.MFFluidTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MinefactorialClient {
	private MinefactorialClient() { }

	@SubscribeEvent
	public static void registerMenus(final RegisterMenuScreensEvent event) {
		event.register(MFMenuTypes.STEAM_BOILER.get(), ScreenSteamBoiler::new);
		event.register(MFMenuTypes.STEAM_TURBINE.get(), ScreenSteamTurbine::new);
		event.register(MFMenuTypes.CAPACITOR_CONTAINER.get(), ScreenCapacitor::new);
		event.register(MFMenuTypes.BREAKER_CONTAINER.get(), ScreenBreaker::new);
	}

	@SubscribeEvent
	public static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
		event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.STEAM.get()), MFFluidTypes.STEAM);
		event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.OIL.get()), MFFluidTypes.OIL);
	}
}
