package martian.minefactorial.foundation.world;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

import java.util.stream.Stream;

public final class AABBHelpers {
	private AABBHelpers() { }

	/**
	 * The same as {@see BlockPos.betweenClosedStream} but excludes {@code minX/Y/Z}.
	 * @param aabb The AABB to stream
	 * @return A stream of {@see BlockPos}s in the AABB.
	 */
	public static Stream<BlockPos> properlyBoundedStreamAABB(AABB aabb) {
		return BlockPos.betweenClosedStream(
				Mth.floor(aabb.minX),
				Mth.floor(aabb.minY),
				Mth.floor(aabb.minZ),
				Mth.floor(aabb.maxX - 1),
				Mth.floor(aabb.maxY),
				Mth.floor(aabb.maxZ - 1)
		).filter(it -> it.getY() == aabb.minY);
		// I know the .filter looks weird, but it works as intended. Modifying the
		// minY/maxY values does not work because it will always iterate at least 2
		// blocks in height. Please do not ask me why it is implemented this way.
	}

	public static AABB ofBlock(BlockPos pos) {
		return AABB.encapsulatingFullBlocks(pos, pos).deflate(1/16F, 0, 1/16F);
	}
}
