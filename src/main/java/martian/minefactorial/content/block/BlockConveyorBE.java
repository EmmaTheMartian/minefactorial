package martian.minefactorial.content.block;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.Mathematics;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlockConveyorBE extends BlockEntity implements ITickableBE {
	public BlockConveyorBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.CONVEYOR.get(), pos, blockState);
	}

	// We move entities over common tick so that entities don't "teleport" on clients
	private void commonTick() {
		assert level != null;

		BlockConveyor.VerticalState verticalState = getBlockState().getValue(BlockConveyor.VERTICAL_STATE);
		Direction direction = getBlockState().getValue(BlockConveyor.FACING);

		for (Entity entity : level.getEntitiesOfClass(Entity.class, getAABB(worldPosition))) {
			if (entity.isCrouching()) {
				continue;
			}
			handleConveyorMovement(entity, worldPosition, direction, verticalState);
//			if (entity instanceof ItemEntity itemEntity) {
//				itemEntity.setPickUpDelay(5);
//			}
		}
	}

	@Override
	public void serverTick() {
		commonTick();
	}

	@Override
	public void clientTick() {
		commonTick();
	}

	protected static AABB getAABB(BlockPos pos) {
		return AABB.encapsulatingFullBlocks(pos, pos).deflate(1/16F, 0, 1/16F);
	}

	// Modified using https://github.com/InnovativeOnlineIndustries/Industrial-Foregoing/blob/ecfc63dcea8d9fe151e13fa5ee5ef0d246dab5b4/src/main/java/com/buuz135/industrial/utils/MovementUtils.java#L38
	public static void handleConveyorMovement(Entity entity, BlockPos pos, Direction direction, BlockConveyor.VerticalState verticalState) {
		if (entity instanceof Player && entity.isCrouching())
			return;

		boolean isVertical = verticalState != BlockConveyor.VerticalState.NONE;
		if (entity.blockPosition().getY() - pos.getY() > 0.3 && isVertical)
			return;

		// Directional movement
		double speed = 0.175f;
		Vec3 vec3d = new Vec3(speed * direction.getNormal().getX(), speed * direction.getNormal().getY(), speed * direction.getNormal().getZ());
		if (isVertical) {
			vec3d = vec3d.add(0, verticalState == BlockConveyor.VerticalState.UP ? 0.258 : verticalState == BlockConveyor.VerticalState.DOWN ? -0.05 : 0, 0);
			entity.setOnGround(false);
		}

		// Center the entity
		if (direction == Direction.NORTH || direction == Direction.SOUTH) {
			if (entity.getX() - pos.getX() < 0.45) {
				vec3d = vec3d.add(0.08, 0, 0);
			} else if (entity.getX() - pos.getX() > 0.55) {
				vec3d = vec3d.add(-0.08, 0, 0);
			}
		}
		if (direction == Direction.EAST || direction == Direction.WEST) {
			if (entity.getZ() - pos.getZ() < 0.45) {
				vec3d = vec3d.add(0, 0, 0.08);
			} else if (entity.getZ() - pos.getZ() > 0.55) {
				vec3d = vec3d.add(0, 0, -0.08);
			}
		}

		// Move the entity
		entity.setDeltaMovement(vec3d.x, vec3d.y != 0 ? vec3d.y : entity.getDeltaMovement().y, vec3d.z);
	}
}
