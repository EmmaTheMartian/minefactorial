package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MFFluids {
	private MFFluids() { }

	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(Registries.FLUID, Minefactorial.MODID);

	private static DeferredHolder<Fluid, FlowingFluid> registerSource(String id, Supplier<BaseFlowingFluid.Properties> properties) {
		return REGISTRY.register(id, () -> new BaseFlowingFluid.Source(properties.get()));
	}

	private static DeferredHolder<Fluid, FlowingFluid> registerFlowing(String id, Supplier<BaseFlowingFluid.Properties> properties) {
		return REGISTRY.register(id, () -> new BaseFlowingFluid.Flowing(properties.get()));
	}

	private static DeferredBlock<LiquidBlock> registerBlock(String id, Supplier<FlowingFluid> sourceSupplier) {
		return MFBlocks.REGISTRY.register(id, () -> new LiquidBlock(sourceSupplier.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable()));
	}

	private static DeferredItem<BucketItem> registerBucket(String id, Supplier<FlowingFluid> sourceSupplier) {
		return MFItems.REGISTRY.register(id, () -> new BucketItem(sourceSupplier.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
	}

	// Steam
	public static DeferredHolder<Fluid, FlowingFluid> STEAM = registerSource("steam", () -> MFFluids.STEAM_PROPERTIES);
	public static DeferredHolder<Fluid, FlowingFluid> STEAM_FLOWING = registerFlowing("steam_flowing", () -> MFFluids.STEAM_PROPERTIES);
	public static DeferredBlock<LiquidBlock> STEAM_BLOCK = registerBlock("steam", MFFluids.STEAM);
	public static DeferredItem<BucketItem> STEAM_BUCKET = registerBucket("steam_bucket", MFFluids.STEAM);

	public static BaseFlowingFluid.Properties STEAM_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.STEAM,
			MFFluids.STEAM,
			MFFluids.STEAM_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.STEAM_BLOCK)
			.bucket(MFFluids.STEAM_BUCKET);

	// Oil
	public static DeferredHolder<Fluid, FlowingFluid> OIL = registerSource("oil", () -> MFFluids.OIL_PROPERTIES);
	public static DeferredHolder<Fluid, FlowingFluid> OIL_FLOWING = registerFlowing("oil_flowing", () -> MFFluids.OIL_PROPERTIES);
	public static DeferredBlock<LiquidBlock> OIL_BLOCK = registerBlock("oil", MFFluids.OIL);
	public static DeferredItem<BucketItem> OIL_BUCKET = registerBucket("oil_bucket", MFFluids.OIL);

	public static BaseFlowingFluid.Properties OIL_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.OIL,
			MFFluids.OIL,
			MFFluids.OIL_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.OIL_BLOCK)
			.bucket(MFFluids.OIL_BUCKET);
}
