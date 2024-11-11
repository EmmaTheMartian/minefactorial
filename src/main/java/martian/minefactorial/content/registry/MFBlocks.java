package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.logistics.*;
import martian.minefactorial.content.block.machinery.*;
import martian.minefactorial.content.block.power.BlockSteamBoiler;
import martian.minefactorial.content.block.power.BlockSteamTurbine;
import martian.minefactorial.content.block.redstone.BlockRedstoneClock;
import martian.minefactorial.content.block.storage.*;
import martian.regolith.DeferredHolders;
import martian.regolith.builder.RegolithBlockBuilder;
import martian.regolith.neoforge.RegolithNeoForge;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MFBlocks {
	private MFBlocks() { }

	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Minefactorial.MODID);

	private static DeferredBlock<?> register(String id, Supplier<Block> supplier) {
		DeferredBlock<?> entry = REGISTRY.register(id, supplier);
		MFItems.REGISTRY.register(id, () -> new BlockItem(entry.get(), new Item.Properties()));
		return entry;
	}

	// Regolith shorthands
	private static RegolithBlockBuilder<DeferredBlock<? extends Block>> getRegolithBuilder(BlockBehaviour.Properties props) {
		return new RegolithBlockBuilder<>(
				RegolithNeoForge.wrapBlocks(REGISTRY),
				RegolithNeoForge.wrapItems(MFItems.REGISTRY),
				props
		);
	}

	private static DeferredHolders<Block, DeferredBlock<? extends Block>> bulkRegister(BlockBehaviour.Properties props, String... ids) {
		return getRegolithBuilder(props).register(ids).done();
	}

	public static final BlockBehaviour.Properties
			MACHINE_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.METAL)
					.mapColor(DyeColor.WHITE)
					.pushReaction(PushReaction.IGNORE)
					.strength(3f)
					.requiresCorrectToolForDrops(),
			PLASTIC_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.POLISHED_DEEPSLATE)
					.mapColor(DyeColor.WHITE)
					.strength(3f)
					.requiresCorrectToolForDrops(),
			ROAD_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.POLISHED_DEEPSLATE)
					.mapColor(DyeColor.WHITE)
					.strength(3f)
					.speedFactor(1.15f)
					.requiresCorrectToolForDrops(),
			PIPE_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.STONE)
					.mapColor(DyeColor.WHITE)
					.strength(2f)
					.isSuffocating((state, level, pos) -> false)
					.noOcclusion(),
			CONVEYOR_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.STONE)
					.mapColor(DyeColor.WHITE)
					.isSuffocating((state, level, pos) -> false)
					.noOcclusion()
					.noCollission()
					.instabreak();

	public static final DeferredBlock<?>
			// Logistics
			CONVEYOR = register("conveyor", () -> new BlockConveyor(CONVEYOR_PROPS)),
			EJECTOR = register("ejector", () -> new BlockEjector(MACHINE_PROPS)),
			FLUID_EXTRACTOR = register("fluid_extractor", () -> new BlockFluidExtractor(MACHINE_PROPS)),
			ENERGY_PIPE = register("energy_pipe", () -> new BlockPipeEnergy(PIPE_PROPS)),
			FLUID_PIPE = register("fluid_pipe", () -> new BlockPipeFluid(PIPE_PROPS)),
			// Machinery
			BREAKER = register("breaker", () -> new BlockBreaker(MACHINE_PROPS)),
			MOB_GRINDER = register("mob_grinder", () -> new BlockMobGrinder(MACHINE_PROPS)),
			FOUNTAIN = register("fountain", () -> new BlockFountain(MACHINE_PROPS)),
			PUMP = register("pump", () -> new BlockPump(MACHINE_PROPS)),
			PLACER = register("placer", () -> new BlockPlacer(MACHINE_PROPS)),
			// Power
			STEAM_BOILER = register("steam_boiler", () -> new BlockSteamBoiler(MACHINE_PROPS)),
			STEAM_TURBINE = register("steam_turbine", () -> new BlockSteamTurbine(MACHINE_PROPS)),
			// Redstone
			REDSTONE_CLOCK = register("redstone_clock", () -> new BlockRedstoneClock(MACHINE_PROPS)),
			// Storage
			CAPACITOR = register("capacitor", () -> new BlockCapacitor(MACHINE_PROPS)),
			CREATIVE_CAPACITOR = register("creative_capacitor", () -> new BlockCreativeCapacitor(MACHINE_PROPS)),
			PLASTIC_TANK = register("plastic_tank", () -> new BlockPlasticTank(MACHINE_PROPS)),
			CREATIVE_TANK = register("creative_tank", () -> new BlockCreativeTank(MACHINE_PROPS)),
			STORAGE_UNIT = register("storage_unit", () -> new BlockStorageUnit(MACHINE_PROPS))
	;

	// Decorative blocks
	public static final DeferredHolders<Block, DeferredBlock<? extends Block>> DECOR_BLOCKS = getRegolithBuilder(PLASTIC_PROPS)
			.register("plastic_block", "smooth_plastic", "plastic_bricks")
			.setBlockFunction(RotatedPillarBlock::new)
			.register("plastic_pillar")
			.done();

	// Roads
	public static final DeferredHolders<Block, DeferredBlock<? extends Block>> ROAD_BLOCKS = bulkRegister(
			ROAD_PROPS,
			"plastic_road"
	);
}
