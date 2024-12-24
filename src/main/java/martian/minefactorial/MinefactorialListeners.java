package martian.minefactorial;

import martian.minefactorial.content.block.logistics.BlockFluidExtractorBE;
import martian.minefactorial.content.block.machinery.husbandry.BlockSlaughterhouseBE;
import martian.minefactorial.content.block.power.BlockSteamBoilerBE;
import martian.minefactorial.content.block.storage.BlockCapacitorBE;
import martian.minefactorial.content.net.PacketServerboundSetSmasherFortuneLevel;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractEnergyBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import martian.minefactorial.foundation.block.ISingleTankBE;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nullable;

final class MinefactorialListeners {
	private static @Nullable RegisterCapabilitiesEvent registerCapabilitiesEvent;

	private MinefactorialListeners() { }

	@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.MOD)
	static final class ModBusEvents {
		@SubscribeEvent
		static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
			registerCapabilitiesEvent = event;

			////// Logistics //////
			// Ejector
			registerItemCapability(MFBlockEntityTypes.EJECTOR.get());
			// Fluid Extractor
			registerFluidCapability(MFBlockEntityTypes.FLUID_EXTRACTOR.get(), BlockFluidExtractorBE::getFluidHandler);
			// Pipes
			registerEnergyCapability(MFBlockEntityTypes.ENERGY_PIPE.get());
			registerFluidCapability(MFBlockEntityTypes.FLUID_PIPE.get());

			////// Machinery //////
			// Breaker
			registerEnergyCapability(MFBlockEntityTypes.BREAKER.get());
			// Mob Grinder
			registerEnergyCapability(MFBlockEntityTypes.MOB_GRINDER.get());
			registerFluidCapability(MFBlockEntityTypes.MOB_GRINDER.get());
			registerItemCapability(MFBlockEntityTypes.MOB_GRINDER.get());
			// Fountain
			registerEnergyCapability(MFBlockEntityTypes.FOUNTAIN.get());
			registerFluidCapability(MFBlockEntityTypes.FOUNTAIN.get());
			// Pump
			registerEnergyCapability(MFBlockEntityTypes.PUMP.get());
			registerFluidCapability(MFBlockEntityTypes.PUMP.get());
			// Placer
			registerEnergyCapability(MFBlockEntityTypes.PLACER.get());
			registerItemCapability(MFBlockEntityTypes.PLACER.get());
			// Smasher
			registerEnergyCapability(MFBlockEntityTypes.SMASHER.get());
			registerItemCapability(MFBlockEntityTypes.SMASHER.get());
			registerFluidCapability(MFBlockEntityTypes.SMASHER.get());
			// Macerator
			registerEnergyCapability(MFBlockEntityTypes.MACERATOR.get());
			registerSidedItemCapability(MFBlockEntityTypes.MACERATOR.get());
			// Planter
			registerEnergyCapability(MFBlockEntityTypes.PLANTER.get());
			registerItemCapability(MFBlockEntityTypes.PLANTER.get());
			// Harvester
			registerEnergyCapability(MFBlockEntityTypes.HARVESTER.get());
			registerItemCapability(MFBlockEntityTypes.HARVESTER.get());
			// Slaughterhouse
			registerEnergyCapability(MFBlockEntityTypes.SLAUGHTERHOUSE.get());
			registerItemCapability(MFBlockEntityTypes.SLAUGHTERHOUSE.get());
			registerFluidCapability(MFBlockEntityTypes.SLAUGHTERHOUSE.get(), BlockSlaughterhouseBE::getTank);
			// Meat Packer
			registerEnergyCapability(MFBlockEntityTypes.MEAT_PACKER.get());
			registerItemCapability(MFBlockEntityTypes.MEAT_PACKER.get());
			registerFluidCapability(MFBlockEntityTypes.MEAT_PACKER.get());

			////// Power //////
			// Steam Boiler
			registerFluidCapability(MFBlockEntityTypes.STEAM_BOILER.get(), BlockSteamBoilerBE::getTankForSide);
			registerItemCapability(MFBlockEntityTypes.STEAM_BOILER.get());
			// Steam Turbine
			registerEnergyCapability(MFBlockEntityTypes.STEAM_TURBINE.get());
			registerFluidCapability(MFBlockEntityTypes.STEAM_TURBINE.get());

			////// Redstone //////
			// Nothing yet :P

			////// Storage //////
			// Capacitor
			registerEnergyCapability(MFBlockEntityTypes.CAPACITOR.get(), BlockCapacitorBE::getEnergyStorage);
			registerItemCapability(MFBlockEntityTypes.CAPACITOR.get());
			// Creative Capacitor
			registerEnergyCapability(MFBlockEntityTypes.CREATIVE_CAPACITOR.get());
			registerItemCapability(MFBlockEntityTypes.CREATIVE_CAPACITOR.get());
			// Plastic Tank
			registerFluidCapability(MFBlockEntityTypes.PLASTIC_TANK.get());
			// Creative Tank
			registerFluidCapability(MFBlockEntityTypes.CREATIVE_TANK.get());
			// Storage Unit
			registerItemCapability(MFBlockEntityTypes.STORAGE_UNIT.get());

			registerCapabilitiesEvent = null;
		}

		@SubscribeEvent
		static void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
			final PayloadRegistrar registrar = event.registrar("1");

			registrar.playToServer(PacketServerboundSetSmasherFortuneLevel.TYPE, PacketServerboundSetSmasherFortuneLevel.STREAM_CODEC, PacketServerboundSetSmasherFortuneLevel::handle);
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

	private static <T extends BlockEntity & ISingleTankBE> void registerFluidCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, (be, direction) -> be.getTank());
	}

	private static <T extends BlockEntity> void registerFluidCapability(BlockEntityType<T> type, ICapabilityProvider<T, Direction, IFluidHandler> capabilityProvider) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, capabilityProvider);
	}

	private static <T extends BlockEntity & IInventoryBE<ItemStackHandler>> void registerItemCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, IInventoryBE::getInventory);
	}

	private static <T extends BlockEntity & IInventoryBE<ItemStackHandler>> void registerSidedItemCapability(BlockEntityType<T> type) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, SidedInvWrapper::new);
	}

	private static <T extends BlockEntity> void registerItemCapability(BlockEntityType<T> type, ICapabilityProvider<T, Direction, IItemHandler> capabilityProvider) {
		assert registerCapabilitiesEvent != null;
		registerCapabilitiesEvent.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, capabilityProvider);
	}
}
