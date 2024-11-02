package martian.minefactorial.foundation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class Mathematics {
	private Mathematics() { }

	public static boolean pointWithinRectangle(int pointX, int pointY, int rectX, int rectY, int rectWidth, int rectHeight) {
		return pointX >= rectX &&
				pointX < rectX + rectWidth &&
				pointY >= rectY &&
				pointY < rectY + rectHeight;
	}

	public static Vec3 getDirectionNormals(Direction direction) {
		return switch (direction) {
			case UP -> new Vec3(0, 1, 0);
			case DOWN -> new Vec3(0, -1, 0);
			case NORTH -> new Vec3(0, 0, -1);
			case EAST -> new Vec3(1, 0, 0);
			case SOUTH -> new Vec3(0, 0, 1);
			case WEST -> new Vec3(-1, 0, 0);
		};
	}
}
