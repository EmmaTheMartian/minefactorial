package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.menu.ContainerBreaker;
import martian.minefactorial.content.menu.ContainerCapacitor;
import martian.minefactorial.content.menu.ContainerSteamBoiler;
import martian.minefactorial.content.menu.ContainerSteamTurbine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MFMenuTypes {
	private MFMenuTypes() { }

	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Minefactorial.MODID);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, IContainerFactory<T> factory) {
		return REGISTRY.register(id, () -> IMenuTypeExtension.create(factory));
	}

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSteamBoiler>> STEAM_BOILER = register("steam_boiler",
			(windowId, inventory, data) -> new ContainerSteamBoiler(windowId, inventory, data.readBlockPos()));

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSteamTurbine>> STEAM_TURBINE = register("steam_turbine",
			(windowId, inventory, data) -> new ContainerSteamTurbine(windowId, inventory, data.readBlockPos()));

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerCapacitor>> CAPACITOR_CONTAINER = register("capacitor",
			(windowId, inventory, data) -> new ContainerCapacitor(windowId, inventory, data.readBlockPos()));

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerBreaker>> BREAKER_CONTAINER = register("breaker",
			(windowId, inventory, data) -> new ContainerBreaker(windowId, inventory, data.readBlockPos()));
}
