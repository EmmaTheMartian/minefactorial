package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.entity.ThrownSafariNet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public final class MFEntityTypes {
	private MFEntityTypes() { }

	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Minefactorial.MODID);

	private static <T extends Entity> DeferredEntityType<T> register(String id, EntityType.Builder<T> builder) {
		return DeferredEntityType.fromDeferredHolder(REGISTRY.register(id, () -> builder.build(id)));
	}

	/**
	 * A wrapper for {@code DeferredHolder<EntityType<?>, BlockEntityType<SomeEntity>>} so that I don't have
	 * to type that every time.
	 * @param <T>
	 */
	@ParametersAreNonnullByDefault
	public static class DeferredEntityType<T extends Entity> extends DeferredHolder<EntityType<?>, EntityType<T>> {
		protected DeferredEntityType(ResourceKey<EntityType<?>> key) {
			super(key);
		}

		public static <T extends Entity> DeferredEntityType<T> fromDeferredHolder(DeferredHolder<EntityType<?>, EntityType<T>> holder) {
			return new DeferredEntityType<>(Objects.requireNonNull(holder.getKey()));
		}
	}

	public static final DeferredEntityType<ThrownSafariNet> THROWN_SAFARI_NET = register("thrown_safari_net",
			EntityType.Builder.<ThrownSafariNet>of(ThrownSafariNet::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10));
}
