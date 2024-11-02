package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.*;
import martian.minefactorial.content.block.pipe.BlockPipeEnergyBE;
import martian.minefactorial.content.block.pipe.BlockPipeFluidBE;
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

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerBE>> BREAKER =
			register("breaker", BlockBreakerBE::new, MFBlocks.BREAKER);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlasticTankBE>> PLASTIC_TANK =
			register("plastic_tank", BlockPlasticTankBE::new, MFBlocks.PLASTIC_TANK);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockMobGrinderBE>> MOB_GRINDER =
			register("mob_grinder", BlockMobGrinderBE::new, MFBlocks.MOB_GRINDER);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockConveyorBE>> CONVEYOR =
			register("conveyor", BlockConveyorBE::new, MFBlocks.CONVEYOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEjectorBE>> EJECTOR =
			register("ejector", BlockEjectorBE::new, MFBlocks.EJECTOR);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockFountainBE>> FOUNTAIN =
			register("fountain", BlockFountainBE::new, MFBlocks.FOUNTAIN);


	// Pipes
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPipeEnergyBE>> ENERGY_PIPE =
			register("energy_pipe", BlockPipeEnergyBE::new, MFBlocks.ENERGY_PIPE);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPipeFluidBE>> FLUID_PIPE =
			register("fluid_pipe", BlockPipeFluidBE::new, MFBlocks.FLUID_PIPE);
}
