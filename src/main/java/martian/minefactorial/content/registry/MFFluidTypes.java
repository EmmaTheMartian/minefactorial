package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.foundation.fluid.BasicFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class MFFluidTypes {
	private MFFluidTypes() { }

	public static final ResourceLocation
			WATER_STILL_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still"),
			WATER_FLOWING = ResourceLocation.withDefaultNamespace("block/water_flowing"),
			WATER_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Minefactorial.MODID);

	private static <T extends FluidType> DeferredHolder<FluidType, T> register(String id, T fluidType) {
		return REGISTRY.register(id, () -> fluidType);
	}

	public static final DeferredHolder<FluidType, BasicFluidType> STEAM = register("steam", new BasicFluidType(
			WATER_STILL_TEXTURE,
			WATER_FLOWING,
			WATER_OVERLAY,
			0xFFEFEFEF,
			new Vector3f(0.8f, 0.8f, 0.8f),
			FluidType.Properties.create()
	));

	public static final DeferredHolder<FluidType, BasicFluidType> OIL = register("oil", new BasicFluidType(
			WATER_STILL_TEXTURE,
			WATER_FLOWING,
			WATER_OVERLAY,
			0xFF555555,
			new Vector3f(0.1f, 0.1f, 0.1f),
			FluidType.Properties.create()
					.density(3000)
					.viscosity(6000)
					.canSwim(false)
			) {
				@Override
				public void setItemMovement(@NotNull ItemEntity entity) {
					Vec3 vec3 = entity.getDeltaMovement();
					entity.setDeltaMovement(vec3.x * (double) 0.95F, vec3.y + (double) (vec3.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3.z * (double) 0.95F);
				}
			});

	public static final DeferredHolder<FluidType, BasicFluidType> ESSENCE = register("essence", new BasicFluidType(
			WATER_STILL_TEXTURE,
			WATER_FLOWING,
			WATER_OVERLAY,
			0xFF44FF44,
			new Vector3f(0.1f, 0.9f, 0.1f),
			FluidType.Properties.create()
	));
}
