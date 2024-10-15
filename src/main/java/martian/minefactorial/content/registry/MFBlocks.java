package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.block.BlockBreaker;
import martian.minefactorial.content.block.BlockCapacitor;
import martian.minefactorial.content.block.BlockSteamBoiler;
import martian.minefactorial.content.block.BlockSteamTurbine;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

	public static final BlockBehaviour.Properties MACHINE_PROPS = BlockBehaviour.Properties.of()
			.sound(SoundType.METAL)
			.mapColor(DyeColor.WHITE)
			.pushReaction(PushReaction.IGNORE)
			.strength(2f);

	public static final DeferredBlock<?>
			STEAM_TURBINE = register("steam_turbine", () -> new BlockSteamTurbine(MACHINE_PROPS)),
			STEAM_BOILER = register("steam_boiler", () -> new BlockSteamBoiler(MACHINE_PROPS)),
			CAPACITOR = register("capacitor", () -> new BlockCapacitor(MACHINE_PROPS)),
			BREAKER = register("breaker", () -> new BlockBreaker(MACHINE_PROPS))
			;
}
