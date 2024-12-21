package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.foliage.BlockRubberWood;
import martian.minefactorial.content.block.logistics.*;
import martian.minefactorial.content.block.machinery.*;
import martian.minefactorial.content.block.machinery.farming.BlockHarvester;
import martian.minefactorial.content.block.machinery.farming.BlockPlanter;
import martian.minefactorial.content.block.machinery.husbandry.BlockMobGrinder;
import martian.minefactorial.content.block.power.BlockSteamBoiler;
import martian.minefactorial.content.block.power.BlockSteamTurbine;
import martian.minefactorial.content.block.redstone.BlockRedstoneClock;
import martian.minefactorial.content.block.storage.*;
import martian.minefactorial.foundation.item.MFBlockItem;
import martian.regolith.DeferredHolders;
import martian.regolith.builder.RegolithBlockBuilder;
import martian.regolith.neoforge.RegolithNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MFBlocks {
	private MFBlocks() { }

	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(Minefactorial.MODID);

	private static DeferredBlock<?> register(String id, Supplier<Block> supplier, String... hoverText) {
		DeferredBlock<?> entry = REGISTRY.register(id, supplier);
		MFItems.REGISTRY.register(id, () -> new MFBlockItem(entry.get(), new Item.Properties(), hoverText));
		return entry;
	}

	// Hover ID shorthands
	private static String[] getHoverTextIdsFor(String id, int lines) {
		String[] ids = new String[lines];
		for (int i = 0; i < lines; i++) {
			ids[i] = String.format("block.%s.%s.desc.%d", Minefactorial.MODID, id, i);
		}
		return ids;
	}

	private static String[] getHoverTextIdsFor(String id) {
		return getHoverTextIdsFor(id, 1);
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

	// Properties and such
	public static Boolean never(BlockState a, BlockGetter b, BlockPos c, EntityType<?> d) { return false; }
	public static Boolean never(BlockState a, BlockGetter b, BlockPos c) { return false; }

	public static final BlockBehaviour.Properties
			MACHINE_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.METAL)
					.mapColor(MapColor.METAL)
					.pushReaction(PushReaction.IGNORE)
					.strength(3f)
					.requiresCorrectToolForDrops(),
			PLASTIC_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.POLISHED_DEEPSLATE)
					.mapColor(MapColor.COLOR_LIGHT_GRAY)
					.strength(3f)
					.requiresCorrectToolForDrops(),
			ROAD_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.POLISHED_DEEPSLATE)
					.mapColor(MapColor.COLOR_GRAY)
					.strength(3f)
					.speedFactor(1.15f)
					.requiresCorrectToolForDrops(),
			PIPE_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.STONE)
					.mapColor(MapColor.METAL)
					.strength(2f)
					.isSuffocating(MFBlocks::never)
					.noOcclusion(),
			CONVEYOR_PROPS = BlockBehaviour.Properties.of()
					.sound(SoundType.STONE)
					.mapColor(MapColor.METAL)
					.isSuffocating(MFBlocks::never)
					.noOcclusion()
					.noCollission(),
			WOOD_PROPS = BlockBehaviour.Properties.of()
					.mapColor(MapColor.WOOD)
					.instrument(NoteBlockInstrument.BASS)
					.strength(2.0F)
					.sound(SoundType.WOOD)
					.ignitedByLava(),
			LEAVES_PROPS = BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.strength(0.2F)
					.randomTicks()
					.sound(SoundType.GRASS)
					.noOcclusion()
					.isValidSpawn(Blocks::ocelotOrParrot)
					.isSuffocating(MFBlocks::never)
					.isViewBlocking(MFBlocks::never)
					.ignitedByLava()
					.pushReaction(PushReaction.DESTROY)
					.isRedstoneConductor(MFBlocks::never),
			SAPLINGS_PROPS = BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.noCollission()
					.randomTicks()
					.instabreak()
					.sound(SoundType.GRASS)
					.pushReaction(PushReaction.DESTROY)
	;

	public static final DeferredBlock<?>
			// Logistics
			CONVEYOR = register("conveyor", () -> new BlockConveyor(CONVEYOR_PROPS), getHoverTextIdsFor("conveyor", 2)),
			HOPPING_CONVEYOR = register("hopping_conveyor", () -> new BlockHoppingConveyor(CONVEYOR_PROPS), getHoverTextIdsFor("hopping_conveyor", 3)),
			EJECTOR = register("ejector", () -> new BlockEjector(MACHINE_PROPS), getHoverTextIdsFor("ejector", 2)),
			FLUID_EXTRACTOR = register("fluid_extractor", () -> new BlockFluidExtractor(MACHINE_PROPS)),
			ENERGY_PIPE = register("energy_pipe", () -> new BlockPipeEnergy(PIPE_PROPS)),
			FLUID_PIPE = register("fluid_pipe", () -> new BlockPipeFluid(PIPE_PROPS)),
			// Machinery
			BREAKER = register("breaker", () -> new BlockBreaker(MACHINE_PROPS)),
			MOB_GRINDER = register("mob_grinder", () -> new BlockMobGrinder(MACHINE_PROPS)),
			FOUNTAIN = register("fountain", () -> new BlockFountain(MACHINE_PROPS)),
			PUMP = register("pump", () -> new BlockPump(MACHINE_PROPS)),
			PLACER = register("placer", () -> new BlockPlacer(MACHINE_PROPS)),
			SMASHER = register("smasher", () -> new BlockSmasher(MACHINE_PROPS)),
			MACERATOR = register("macerator", () -> new BlockMacerator(MACHINE_PROPS)),
			PLANTER = register("planter", () -> new BlockPlanter(MACHINE_PROPS)),
			HARVESTER = register("harvester", () -> new BlockHarvester(MACHINE_PROPS)),
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
			STORAGE_UNIT = register("storage_unit", () -> new BlockStorageUnit(MACHINE_PROPS)),
			// Foliage
			RUBBER_WOOD = register("rubber_wood", () -> new BlockRubberWood(WOOD_PROPS)),
			RUBBER_LEAVES = register("rubber_leaves", () -> new LeavesBlock(LEAVES_PROPS)),
			RUBBER_SAPLING = register("rubber_sapling", () -> new SaplingBlock(BlockRubberWood.TREE_GROWER, SAPLINGS_PROPS)),
			// Misc
			MACHINE_FRAME = register("machine_frame", () -> new Block(MACHINE_PROPS))
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
