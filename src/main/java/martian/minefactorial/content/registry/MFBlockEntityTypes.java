package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.logistics.*;
import martian.minefactorial.content.block.machinery.*;
import martian.minefactorial.content.block.power.BlockSteamBoilerBE;
import martian.minefactorial.content.block.power.BlockSteamTurbineBE;
import martian.minefactorial.content.block.redstone.BlockRedstoneClockBE;
import martian.minefactorial.content.block.storage.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class MFBlockEntityTypes {
	private MFBlockEntityTypes() { }

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Minefactorial.MODID);

	private static <T extends BlockEntity> DeferredBEType<T> register(String id, Supplier<BlockEntityType<T>> supplier) {
		return DeferredBEType.fromDeferredHolder(REGISTRY.register(id, supplier));
	}

	private static <T extends BlockEntity> DeferredBEType<T> register(String id, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory, DeferredBlock<?>... validBlockHolders) {
		return DeferredBEType.fromDeferredHolder(REGISTRY.register(id, () -> new BlockEntityType<>(
				blockEntityFactory,
				Arrays.stream(validBlockHolders).map(DeferredBlock::get).collect(Collectors.toSet()),
				null
		)));
	}

	/**
	 * A wrapper for {@code DeferredHolder<BlockEntityType<?>, BlockEntityType<SomeBlockEntity>>} so that I don't have
	 * to type that every time.
	 * @param <T>
	 */
	@ParametersAreNonnullByDefault
	public static class DeferredBEType<T extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> {
		protected DeferredBEType(ResourceKey<BlockEntityType<?>> key) {
			super(key);
		}

		public static <T extends BlockEntity> DeferredBEType<T> fromDeferredHolder(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder) {
			return new DeferredBEType<>(Objects.requireNonNull(holder.getKey()));
		}
	}

	// Logistics
	public static final DeferredBEType<BlockConveyorBE> CONVEYOR = register("conveyor", BlockConveyorBE::new, MFBlocks.CONVEYOR);
	public static final DeferredBEType<BlockEjectorBE> EJECTOR = register("ejector", BlockEjectorBE::new, MFBlocks.EJECTOR);
	public static final DeferredBEType<BlockFluidExtractorBE> FLUID_EXTRACTOR = register("fluid_extractor", BlockFluidExtractorBE::new, MFBlocks.FLUID_EXTRACTOR);
	public static final DeferredBEType<BlockPipeEnergyBE> ENERGY_PIPE = register("energy_pipe", BlockPipeEnergyBE::new, MFBlocks.ENERGY_PIPE);
	public static final DeferredBEType<BlockPipeFluidBE> FLUID_PIPE = register("fluid_pipe", BlockPipeFluidBE::new, MFBlocks.FLUID_PIPE);
	// Machine
	public static final DeferredBEType<BlockBreakerBE> BREAKER = register("breaker", BlockBreakerBE::new, MFBlocks.BREAKER);
	public static final DeferredBEType<BlockMobGrinderBE> MOB_GRINDER = register("mob_grinder", BlockMobGrinderBE::new, MFBlocks.MOB_GRINDER);
	public static final DeferredBEType<BlockFountainBE> FOUNTAIN = register("fountain", BlockFountainBE::new, MFBlocks.FOUNTAIN);
	public static final DeferredBEType<BlockPumpBE> PUMP = register("pump", BlockPumpBE::new, MFBlocks.PUMP);
	public static final DeferredBEType<BlockPlacerBE> PLACER = register("placer", BlockPlacerBE::new, MFBlocks.PLACER);
	// Power
	public static final DeferredBEType<BlockSteamTurbineBE> STEAM_TURBINE = register("steam_turbine", BlockSteamTurbineBE::new, MFBlocks.STEAM_TURBINE);
	public static final DeferredBEType<BlockSteamBoilerBE> STEAM_BOILER = register("steam_boiler", BlockSteamBoilerBE::new, MFBlocks.STEAM_BOILER);
	// Redstone
	public static final DeferredBEType<BlockRedstoneClockBE> REDSTONE_CLOCK = register("redstone_clock", BlockRedstoneClockBE::new, MFBlocks.REDSTONE_CLOCK);
	// Storage
	public static final DeferredBEType<BlockCapacitorBE> CAPACITOR = register("capacitor", BlockCapacitorBE::new, MFBlocks.CAPACITOR);
	public static final DeferredBEType<BlockCreativeCapacitorBE> CREATIVE_CAPACITOR = register("creative_capacitor", BlockCreativeCapacitorBE::new, MFBlocks.CREATIVE_CAPACITOR);
	public static final DeferredBEType<BlockPlasticTankBE> PLASTIC_TANK = register("plastic_tank", BlockPlasticTankBE::new, MFBlocks.PLASTIC_TANK);
	public static final DeferredBEType<BlockCreativeTankBE> CREATIVE_TANK = register("creative_tank", BlockCreativeTankBE::new, MFBlocks.CREATIVE_TANK);
	public static final DeferredBEType<BlockStorageUnitBE> STORAGE_UNIT = register("storage_unit", BlockStorageUnitBE::new, MFBlocks.STORAGE_UNIT);
}
