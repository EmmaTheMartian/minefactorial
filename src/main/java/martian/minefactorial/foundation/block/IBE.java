package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Default methods for a block entity, this is just used by other interfaces.
 */
public interface IBE {
	BlockPos getBlockPos();
	BlockState getBlockState();
	Level getLevel();
	void setChanged();
}
