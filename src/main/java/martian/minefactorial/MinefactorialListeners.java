package martian.minefactorial;

import martian.minefactorial.content.block.BlockCapacitorBE;
import martian.minefactorial.content.block.BlockSteamBoilerBE;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.*;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

final class MinefactorialListeners {
	private static @Nullable RegisterCapabilitiesEvent registerCapabilitiesEvent;

	private MinefactorialListeners() { }

	@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.MOD)
	static final class ModBusEvents {
		@SubscribeEvent
		static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
			registerCapabilitiesEvent = event;

			// Steam Turbine
			registerEnergyCapability(MFBlockEntityTypes.STEAM_TURBINE.get());
			registerSingleFluidCapability(MFBlockEntityTypes.STEAM_TURBINE.get());

			// Steam Boiler
			registerSingleFluidCapability(MFBlockEntityTypes.STEAM_BOILER.get(), BlockSteamBoilerBE::getTankForSide);
			registerItemCapability(MFBlockEntityTypes.STEAM_BOILER.get());

			// Capacitor
			registerEnergyCapability(MFBlockEntityTypes.CAPACITOR.get(), BlockCapacitorBE::getEnergyStorage);
			registerItemCapability(MFBlockEntityTypes.CAPACITOR.get());

			// Breaker
			registerEnergyCapability(MFBlockEntityTypes.BREAKER.get());

			// Plastic Tank
			registerSingleFluidCapability(MFBlockEntityTypes.PLASTIC_TANK.get());

			// Mob Grinder
			registerEnergyCapability(MFBlockEntityTypes.MOB_GRINDER.get());
			registerSingleFluidCapability(MFBlockEntityTypes.MOB_GRINDER.get());
			registerItemCapability(MFBlockEntityTypes.MOB_GRINDER.get());

			// Ejector
			registerItemCapability(MFBlockEntityTypes.EJECTOR.get());

			// Fountain
			registerEnergyCapability(MFBlockEntityTypes.FOUNTAIN.get());
			registerSingleFluidCapability(MFBlockEntityTypes.FOUNTAIN.get());

			// Pipes
			registerEnergyCapability(MFBlockEntityTypes.ENERGY_PIPE.get());
			registerSingleFluidCapability(MFBlockEntityTypes.FLUID_PIPE.get());

			registerCapabilitiesEvent = null;
		}
	}

//	@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.GAME)
//	static final class GameBusEvents { }

	// Helpers
	private static <T extends AbstractEnergyBE> void registerEnergyCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, (be, direction) -> be.getEnergyStorage());
	}

	private static <T extends BlockEntity> void registerEnergyCapability(BlockEntityType<T> type, ICapabilityProvider<T, Direction, IEnergyStorage> capabilityProvider) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, capabilityProvider);
	}

	private static <T extends BlockEntity & ISingleTankBE> void registerSingleFluidCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, (be, direction) -> be.getTank());
	}

	private static <T extends BlockEntity> void registerSingleFluidCapability(BlockEntityType<T> type, ICapabilityProvider<T, Direction, IFluidHandler> capabilityProvider) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, capabilityProvider);
	}

	private static <T extends BlockEntity & IInventoryBE> void registerItemCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (be, direction) -> be.getInventory());
	}

	private static <T extends BlockEntity> void registerItemCapability(BlockEntityType<T> type, ICapabilityProvider<T, Direction, IItemHandler> capabilityProvider) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, capabilityProvider);
	}
}
