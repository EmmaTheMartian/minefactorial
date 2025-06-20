package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.menu.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;

public final class MFMenuTypes {
	private MFMenuTypes() { }

	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, Minefactorial.MODID);

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, IContainerFactory<T> factory) {
		return REGISTRY.register(id, () -> IMenuTypeExtension.create(factory));
	}

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerSimple(String id, TriFunction<Integer, Inventory, BlockPos, T> factory) {
		return REGISTRY.register(id, () -> IMenuTypeExtension.create((windowId, inventory, buf) ->
				factory.apply(windowId, inventory, buf.readBlockPos())));
	}

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSteamBoiler>> STEAM_BOILER =
			registerSimple("steam_boiler", ContainerSteamBoiler::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSteamTurbine>> STEAM_TURBINE =
			registerSimple("steam_turbine", ContainerSteamTurbine::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerCapacitor>> CAPACITOR =
			registerSimple("capacitor", ContainerCapacitor::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerCreativeCapacitor>> CREATIVE_CAPACITOR =
			registerSimple("creative_capacitor", ContainerCreativeCapacitor::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerBreaker>> BREAKER =
			registerSimple("breaker", ContainerBreaker::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerMobGrinder>> MOB_GRINDER =
			registerSimple("mob_grinder", ContainerMobGrinder::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerFountain>> FOUNTAIN =
			registerSimple("fountain", ContainerFountain::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerPump>> PUMP =
			registerSimple("pump", ContainerPump::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerPlacer>> PLACER =
			registerSimple("placer", ContainerPlacer::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSmasher>> SMASHER =
			registerSimple("smasher", ContainerSmasher::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerMacerator>> MACERATOR =
			registerSimple("macerator", ContainerMacerator::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerPlanter>> PLANTER =
			registerSimple("planter", ContainerPlanter::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerHarvester>> HARVESTER =
			registerSimple("harvester", ContainerHarvester::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSlaughterhouse>> SLAUGHTERHOUSE =
			registerSimple("slaughterhouse", ContainerSlaughterhouse::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerMeatPacker>> MEAT_PACKER =
			registerSimple("meat_packer", ContainerMeatPacker::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerItemRouter>> ITEM_ROUTER =
			registerSimple("item_router", ContainerItemRouter::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerRancher>> RANCHER =
			registerSimple("rancher", ContainerRancher::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerFertilizer>> FERTILIZER =
			registerSimple("fertilizer", ContainerFertilizer::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerSewageCollector>> SEWAGE_COLLECTOR =
			registerSimple("sewage_collector", ContainerSewageCollector::new);

	public static final DeferredHolder<MenuType<?>, MenuType<ContainerIndustrialComposter>> INDUSTRIAL_COMPOSTER =
			registerSimple("industrial_composter", ContainerIndustrialComposter::new);
}
