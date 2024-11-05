package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.logistics.*;
import martian.minefactorial.content.block.machinery.*;
import martian.minefactorial.content.block.power.BlockSteamBoilerBE;
import martian.minefactorial.content.block.power.BlockSteamTurbineBE;
import martian.minefactorial.content.block.storage.BlockCapacitorBE;
import martian.minefactorial.content.block.storage.BlockCreativeTankBE;
import martian.minefactorial.content.block.storage.BlockPlasticTankBE;
import martian.minefactorial.content.block.storage.BlockCreativeCapacitorBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class MFBlockEntityTypes {
	private MFBlockEntityTypes() { }

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Minefactorial.MODID);

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> supplier) {
		return REGISTRY.register(id, supplier);
	}

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory, DeferredBlock<?>... validBlockHolders) {
		return REGISTRY.register(id, () -> new BlockEntityType<>(blockEntityFactory, Arrays.stream(validBlockHolders).map(DeferredBlock::get).collect(Collectors.toSet()), null));
	}

	// Machines
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSteamTurbineBE>> STEAM_TURBINE =
			register("steam_turbine", BlockSteamTurbineBE::new, MFBlocks.STEAM_TURBINE);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockSteamBoilerBE>> STEAM_BOILER =
			register("steam_boiler", BlockSteamBoilerBE::new, MFBlocks.STEAM_BOILER);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockCapacitorBE>> CAPACITOR =
			register("capacitor", BlockCapacitorBE::new, MFBlocks.CAPACITOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockCreativeCapacitorBE>> CREATIVE_CAPACITOR =
			register("creative_capacitor", BlockCreativeCapacitorBE::new, MFBlocks.CREATIVE_CAPACITOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerBE>> BREAKER =
			register("breaker", BlockBreakerBE::new, MFBlocks.BREAKER);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlasticTankBE>> PLASTIC_TANK =
			register("plastic_tank", BlockPlasticTankBE::new, MFBlocks.PLASTIC_TANK);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockCreativeTankBE>> CREATIVE_TANK =
			register("creative_tank", BlockCreativeTankBE::new, MFBlocks.CREATIVE_TANK);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockMobGrinderBE>> MOB_GRINDER =
			register("mob_grinder", BlockMobGrinderBE::new, MFBlocks.MOB_GRINDER);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockConveyorBE>> CONVEYOR =
			register("conveyor", BlockConveyorBE::new, MFBlocks.CONVEYOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEjectorBE>> EJECTOR =
			register("ejector", BlockEjectorBE::new, MFBlocks.EJECTOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockFountainBE>> FOUNTAIN =
			register("fountain", BlockFountainBE::new, MFBlocks.FOUNTAIN);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPumpBE>> PUMP =
			register("pump", BlockPumpBE::new, MFBlocks.PUMP);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockFluidExtractorBE>> FLUID_EXTRACTOR =
			register("fluid_extractor", BlockFluidExtractorBE::new, MFBlocks.FLUID_EXTRACTOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBE>> PLACER =
			register("placer", BlockPlacerBE::new, MFBlocks.PLACER);


	// Pipes
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPipeEnergyBE>> ENERGY_PIPE =
			register("energy_pipe", BlockPipeEnergyBE::new, MFBlocks.ENERGY_PIPE);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPipeFluidBE>> FLUID_PIPE =
			register("fluid_pipe", BlockPipeFluidBE::new, MFBlocks.FLUID_PIPE);
}
