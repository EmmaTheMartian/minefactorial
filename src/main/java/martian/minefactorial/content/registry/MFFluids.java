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
	public static final DeferredHolder<Fluid, FlowingFluid> STEAM = registerSource("steam", () -> MFFluids.STEAM_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> STEAM_FLOWING = registerFlowing("steam_flowing", () -> MFFluids.STEAM_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> STEAM_BLOCK = registerBlock("steam", MFFluids.STEAM);
	public static final DeferredItem<BucketItem> STEAM_BUCKET = registerBucket("steam_bucket", MFFluids.STEAM);

	public static final BaseFlowingFluid.Properties STEAM_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.STEAM,
			MFFluids.STEAM,
			MFFluids.STEAM_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.STEAM_BLOCK)
			.bucket(MFFluids.STEAM_BUCKET);

	// Oil
	public static final DeferredHolder<Fluid, FlowingFluid> OIL = registerSource("oil", () -> MFFluids.OIL_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> OIL_FLOWING = registerFlowing("oil_flowing", () -> MFFluids.OIL_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> OIL_BLOCK = registerBlock("oil", MFFluids.OIL);
	public static final DeferredItem<BucketItem> OIL_BUCKET = registerBucket("oil_bucket", MFFluids.OIL);

	public static final BaseFlowingFluid.Properties OIL_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.OIL,
			MFFluids.OIL,
			MFFluids.OIL_FLOWING
	)
			.slopeFindDistance(1)
			.levelDecreasePerBlock(2)
			.block(MFFluids.OIL_BLOCK)
			.bucket(MFFluids.OIL_BUCKET);

	// Essence
	public static final DeferredHolder<Fluid, FlowingFluid> ESSENCE = registerSource("essence", () -> MFFluids.ESSENCE_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> ESSENCE_FLOWING = registerFlowing("essence_flowing", () -> MFFluids.ESSENCE_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> ESSENCE_BLOCK = registerBlock("essence", MFFluids.ESSENCE);
	public static final DeferredItem<BucketItem> ESSENCE_BUCKET = registerBucket("essence_bucket", MFFluids.ESSENCE);

	public static final BaseFlowingFluid.Properties ESSENCE_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.ESSENCE,
			MFFluids.ESSENCE,
			MFFluids.ESSENCE_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.ESSENCE_BLOCK)
			.bucket(MFFluids.ESSENCE_BUCKET);

	// Beetroot Soup
	public static final DeferredHolder<Fluid, FlowingFluid> BEETROOT_SOUP = registerSource("beetroot_soup", () -> MFFluids.BEETROOT_SOUP_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> BEETROOT_SOUP_FLOWING = registerFlowing("beetroot_soup_flowing", () -> MFFluids.BEETROOT_SOUP_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> BEETROOT_SOUP_BLOCK = registerBlock("beetroot_soup", MFFluids.BEETROOT_SOUP);
	public static final DeferredItem<BucketItem> BEETROOT_SOUP_BUCKET = registerBucket("beetroot_soup_bucket", MFFluids.BEETROOT_SOUP);

	public static final BaseFlowingFluid.Properties BEETROOT_SOUP_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.BEETROOT_SOUP,
			MFFluids.BEETROOT_SOUP,
			MFFluids.BEETROOT_SOUP_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.BEETROOT_SOUP_BLOCK)
			.bucket(MFFluids.BEETROOT_SOUP_BUCKET);

	// Mushroom Stew
	public static final DeferredHolder<Fluid, FlowingFluid> MUSHROOM_STEW = registerSource("mushroom_stew", () -> MFFluids.MUSHROOM_STEW_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> MUSHROOM_STEW_FLOWING = registerFlowing("mushroom_stew_flowing", () -> MFFluids.MUSHROOM_STEW_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> MUSHROOM_STEW_BLOCK = registerBlock("mushroom_stew", MFFluids.MUSHROOM_STEW);
	public static final DeferredItem<BucketItem> MUSHROOM_STEW_BUCKET = registerBucket("mushroom_stew_bucket", MFFluids.MUSHROOM_STEW);

	public static final BaseFlowingFluid.Properties MUSHROOM_STEW_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.MUSHROOM_STEW,
			MFFluids.MUSHROOM_STEW,
			MFFluids.MUSHROOM_STEW_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.MUSHROOM_STEW_BLOCK)
			.bucket(MFFluids.MUSHROOM_STEW_BUCKET);

	// Suspicious Stew
	public static final DeferredHolder<Fluid, FlowingFluid> SUSPICIOUS_STEW = registerSource("suspicious_stew", () -> MFFluids.SUSPICIOUS_STEW_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> SUSPICIOUS_STEW_FLOWING = registerFlowing("suspicious_stew_flowing", () -> MFFluids.SUSPICIOUS_STEW_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> SUSPICIOUS_STEW_BLOCK = registerBlock("suspicious_stew", MFFluids.SUSPICIOUS_STEW);
	public static final DeferredItem<BucketItem> SUSPICIOUS_STEW_BUCKET = registerBucket("suspicious_stew_bucket", MFFluids.SUSPICIOUS_STEW);

	public static final BaseFlowingFluid.Properties SUSPICIOUS_STEW_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.SUSPICIOUS_STEW,
			MFFluids.SUSPICIOUS_STEW,
			MFFluids.SUSPICIOUS_STEW_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.SUSPICIOUS_STEW_BLOCK)
			.bucket(MFFluids.SUSPICIOUS_STEW_BUCKET);

	// Rabbit Stew
	public static final DeferredHolder<Fluid, FlowingFluid> RABBIT_STEW = registerSource("rabbit_stew", () -> MFFluids.RABBIT_STEW_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> RABBIT_STEW_FLOWING = registerFlowing("rabbit_stew_flowing", () -> MFFluids.RABBIT_STEW_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> RABBIT_STEW_BLOCK = registerBlock("rabbit_stew", MFFluids.RABBIT_STEW);
	public static final DeferredItem<BucketItem> RABBIT_STEW_BUCKET = registerBucket("rabbit_stew_bucket", MFFluids.RABBIT_STEW);

	public static final BaseFlowingFluid.Properties RABBIT_STEW_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.RABBIT_STEW,
			MFFluids.RABBIT_STEW,
			MFFluids.RABBIT_STEW_FLOWING
	)
			.slopeFindDistance(2)
			.levelDecreasePerBlock(1)
			.block(MFFluids.RABBIT_STEW_BLOCK)
			.bucket(MFFluids.RABBIT_STEW_BUCKET);

	// Honey
	public static final DeferredHolder<Fluid, FlowingFluid> HONEY = registerSource("honey", () -> MFFluids.HONEY_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> HONEY_FLOWING = registerFlowing("honey_flowing", () -> MFFluids.HONEY_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> HONEY_BLOCK = registerBlock("honey", MFFluids.HONEY);
	public static final DeferredItem<BucketItem> HONEY_BUCKET = registerBucket("honey_bucket", MFFluids.HONEY);

	public static final BaseFlowingFluid.Properties HONEY_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.HONEY,
			MFFluids.HONEY,
			MFFluids.HONEY_FLOWING
	)
			.slopeFindDistance(1)
			.levelDecreasePerBlock(2)
			.block(MFFluids.HONEY_BLOCK)
			.bucket(MFFluids.HONEY_BUCKET);

	// Pink Slime
	public static final DeferredHolder<Fluid, FlowingFluid> PINK_SLIME = registerSource("pink_slime", () -> MFFluids.PINK_SLIME_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> PINK_SLIME_FLOWING = registerFlowing("pink_slime_flowing", () -> MFFluids.PINK_SLIME_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> PINK_SLIME_BLOCK = registerBlock("pink_slime", MFFluids.PINK_SLIME);
	public static final DeferredItem<BucketItem> PINK_SLIME_BUCKET = registerBucket("pink_slime_bucket", MFFluids.PINK_SLIME);

	public static final BaseFlowingFluid.Properties PINK_SLIME_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.PINK_SLIME,
			MFFluids.PINK_SLIME,
			MFFluids.PINK_SLIME_FLOWING
	)
			.slopeFindDistance(1)
			.levelDecreasePerBlock(2)
			.block(MFFluids.PINK_SLIME_BLOCK)
			.bucket(MFFluids.PINK_SLIME_BUCKET);

	// Meat
	public static final DeferredHolder<Fluid, FlowingFluid> MEAT = registerSource("meat", () -> MFFluids.MEAT_PROPERTIES);
	public static final DeferredHolder<Fluid, FlowingFluid> MEAT_FLOWING = registerFlowing("meat_flowing", () -> MFFluids.MEAT_PROPERTIES);
	public static final DeferredBlock<LiquidBlock> MEAT_BLOCK = registerBlock("meat", MFFluids.MEAT);
	public static final DeferredItem<BucketItem> MEAT_BUCKET = registerBucket("meat_bucket", MFFluids.MEAT);

	public static final BaseFlowingFluid.Properties MEAT_PROPERTIES = new BaseFlowingFluid.Properties(
			MFFluidTypes.MEAT,
			MFFluids.MEAT,
			MFFluids.MEAT_FLOWING
	)
			.slopeFindDistance(1)
			.levelDecreasePerBlock(2)
			.block(MFFluids.MEAT_BLOCK)
			.bucket(MFFluids.MEAT_BUCKET);
}
