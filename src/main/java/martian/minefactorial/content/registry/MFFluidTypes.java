package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.foundation.fluid.BasicFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
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
			WATER_FLOWING = ResourceLocation.withDefaultNamespace("block/water_flow"),
			WATER_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

	public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Minefactorial.MODID);

	private static <T extends FluidType> DeferredHolder<FluidType, T> register(String id, T fluidType) {
		return REGISTRY.register(id, () -> fluidType);
	}

	private static DeferredHolder<FluidType, BasicFluidType> registerSimple(String id, int colour, FluidType.Properties props) {
		return REGISTRY.register(id, () -> new BasicFluidType(
				WATER_STILL_TEXTURE,
				WATER_FLOWING,
				WATER_OVERLAY,
				colour,
				colourFromHex(colour),
				props
		));
	}

	private static DeferredHolder<FluidType, BasicFluidType> registerSimple(String id, int colour) {
		return registerSimple(id, colour, FluidType.Properties.create());
	}

	private static Vector3f colourFromHex(int colour) {
		return new Vector3f(
				FastColor.ARGB32.red(colour),
				FastColor.ARGB32.green(colour),
				FastColor.ARGB32.blue(colour)
		);
	}

	public static final DeferredHolder<FluidType, BasicFluidType> STEAM = registerSimple("steam", 0xFFEFEFEF);

	public static final DeferredHolder<FluidType, BasicFluidType> OIL = register("oil", new BasicFluidType(
			WATER_STILL_TEXTURE,
			WATER_FLOWING,
			WATER_OVERLAY,
			0xFF555555,
			colourFromHex(0xFF555555),
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

	public static final DeferredHolder<FluidType, BasicFluidType> ESSENCE = registerSimple("essence", 0xFF44FF44);

	public static final DeferredHolder<FluidType, BasicFluidType> BEETROOT_SOUP = registerSimple("beetroot_soup", 0xFF84160D);

	public static final DeferredHolder<FluidType, BasicFluidType> MUSHROOM_STEW = registerSimple("mushroom_stew", 0xFFBE785E);

	public static final DeferredHolder<FluidType, BasicFluidType> SUSPICIOUS_STEW = registerSimple("suspicious_stew", 0xFFC5A45E);

	public static final DeferredHolder<FluidType, BasicFluidType> RABBIT_STEW = registerSimple("rabbit_stew", 0xFFE29D4A);

	public static final DeferredHolder<FluidType, BasicFluidType> HONEY = registerSimple("honey", 0xFFFF9116);
}
