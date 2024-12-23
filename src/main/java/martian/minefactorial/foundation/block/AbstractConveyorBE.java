package martian.minefactorial.foundation.block;

import martian.minefactorial.foundation.world.AABBHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractConveyorBE extends BlockEntity implements ITickableBE {
	public AbstractConveyorBE(BlockEntityType<? extends AbstractConveyorBE> blockEntityType, BlockPos pos, BlockState blockState) {
		super(blockEntityType, pos, blockState);
	}

	protected double getSpeed() {
		return 0.175d;
	}

	// We move entities over common tick so that entities don't "teleport" on clients
	protected void commonTick(Level level) {
		AbstractConveyorBlock.VerticalState verticalState = getBlockState().getValue(AbstractConveyorBlock.VERTICAL_STATE);
		Direction direction = getBlockState().getValue(AbstractConveyorBlock.FACING);

		for (Entity entity : level.getEntitiesOfClass(Entity.class, AABBHelpers.ofBlock(worldPosition))) {
			if (entity.isCrouching() || entity instanceof Player player && player.getAbilities().flying) {
				continue;
			}
			handleConveyorMovement(entity, worldPosition, direction, verticalState);
//			if (entity instanceof ItemEntity itemEntity) {
//				itemEntity.setPickUpDelay(5);
//			}
		}
	}

	@Override
	public void serverTick(ServerLevel level) {
		commonTick(level);
	}

	@Override
	public void clientTick(ClientLevel level) {
		commonTick(level);
	}

	// Modified using https://github.com/InnovativeOnlineIndustries/Industrial-Foregoing/blob/ecfc63dcea8d9fe151e13fa5ee5ef0d246dab5b4/src/main/java/com/buuz135/industrial/utils/MovementUtils.java#L38
	public void handleConveyorMovement(Entity entity, BlockPos pos, Direction direction, AbstractConveyorBlock.VerticalState verticalState) {
		if (entity instanceof Player && entity.isCrouching())
			return;

		boolean isVertical = verticalState != AbstractConveyorBlock.VerticalState.NONE;
		if (entity.blockPosition().getY() - pos.getY() > 0.3 && isVertical)
			return;

		// Directional movement
		double speed = getSpeed();
		Vec3 vec3d = new Vec3(speed * direction.getNormal().getX(), speed * direction.getNormal().getY(), speed * direction.getNormal().getZ());
		if (isVertical) {
			vec3d = vec3d.add(0, verticalState == AbstractConveyorBlock.VerticalState.UP ? 0.258 : verticalState == AbstractConveyorBlock.VerticalState.DOWN ? -0.05 : 0, 0);
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
