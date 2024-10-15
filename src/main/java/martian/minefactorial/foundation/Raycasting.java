package martian.minefactorial.foundation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public final class Raycasting {
	private Raycasting() { }

	public static @Nullable BlockHitResult blockRaycast(Entity entity, double reach, boolean hitFluids) {
		HitResult hit = entity.pick(reach, 0, hitFluids);
		if (hit.getType() != HitResult.Type.BLOCK) {
			return null;
		}
		return (BlockHitResult) hit;
	}

	public static @Nullable EntityHitResult entityRaycast(Entity entity, double reach, boolean hitFluids) {
		HitResult hit = entity.pick(reach, 0, hitFluids);
		if (hit.getType() != HitResult.Type.ENTITY) {
			return null;
		}
		return (EntityHitResult) hit;
	}

	public static HitResult raycast(Entity entity, double reach, boolean hitFluids) {
		return entity.pick(reach, 0, hitFluids);
	}
}
