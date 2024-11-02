package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public interface BlockEntityFactory<T> extends BiFunction<BlockPos, BlockState, T> {
}
