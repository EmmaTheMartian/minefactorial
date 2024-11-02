package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IZonedBE extends IBE {
	default AABB getWorkZone() {
		BlockPos origin = getBlockPos();

		boolean hasFacing = false;
		Direction facing = Direction.NORTH;
		if (getBlockState().hasProperty(DirectionalBlock.FACING)) {
			hasFacing = true;
			facing = getBlockState().getValue(DirectionalBlock.FACING);
		} else if (getBlockState().hasProperty(HorizontalDirectionalBlock.FACING)) {
			hasFacing = true;
			facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		}

		if (hasFacing) {
			origin = origin.relative(facing, getWorkZoneRange() + 1);
		}

		BlockPos start = origin
				.relative(facing.getOpposite(), getWorkZoneRange())
				.relative(facing.getCounterClockWise(), getWorkZoneRange());
		BlockPos end = origin
				.relative(facing, getWorkZoneRange())
				.relative(facing.getClockWise(), getWorkZoneRange())
				.relative(Direction.UP, getWorkZoneHeight() - 1);

		return AABB.encapsulatingFullBlocks(start, end);
	}

	default int getWorkZoneRange() {
		return 1;
	}

	default int getWorkZoneHeight() { return 1; }

	Lazy<AABB> getCachedWorkZone();

	default void invalidateWorkZone() {
		getCachedWorkZone().invalidate();
	}

	default <T extends Entity> Optional<T> getFirstEntityInWorkZone(Class<T> class_, @Nullable Predicate<T> filter) {
		return getEntitiesInWorkZone(class_, filter).stream().findFirst();
	}

	default <T extends Entity> List<T> getEntitiesInWorkZone(Class<T> class_, @Nullable Predicate<T> filter) {
		List<T> found = getLevel().getEntitiesOfClass(class_, getCachedWorkZone().get());
		if (filter != null) {
			found.removeIf(filter.negate());
		}
		return found;
	}

	Predicate<Entity> IS_PLAYER = e -> e instanceof Player;
	Predicate<Entity> NOT_PLAYER = Predicate.not(IS_PLAYER);
	Predicate<Entity> IS_ITEM = e -> e instanceof ItemEntity;
	Predicate<Entity> NOT_ITEM = Predicate.not(IS_ITEM);
	Predicate<LivingEntity> IS_BABY = LivingEntity::isBaby;
	Predicate<LivingEntity> NOT_BABY = Predicate.not(IS_BABY);
	Predicate<LivingEntity> IS_ADULT_ANIMAL = NOT_BABY.and(NOT_PLAYER).and(NOT_ITEM);
	Predicate<LivingEntity> IS_BABY_ANIMAL = IS_BABY.and(NOT_PLAYER).and(NOT_ITEM);
}
