package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.item.ItemTweakeruler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public final class MFDataComponents {
	private MFDataComponents() { }

	public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Minefactorial.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Optional<BlockPos>>> POS = REGISTRY.registerComponentType(
			"pos",
			builder -> builder
					.networkSynchronized(ByteBufCodecs.optional(BlockPos.STREAM_CODEC))
	);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemTweakeruler.Mode>> TWEAKERULER_MODE = REGISTRY.registerComponentType(
			"tweakeruler_mode",
			builder -> builder
					.persistent(ItemTweakeruler.Mode.CODEC)
					.networkSynchronized(ItemTweakeruler.Mode.STREAM_CODEC)
	);
}
