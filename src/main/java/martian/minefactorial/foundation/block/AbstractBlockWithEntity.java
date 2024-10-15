package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public abstract class AbstractBlockWithEntity<T extends BlockEntity> extends Block implements EntityBlock {
	private final BiFunction<BlockPos, BlockState, T> blockEntityFactory;

	public AbstractBlockWithEntity(
			BiFunction<BlockPos, BlockState, T> blockEntityFactory,
			Properties properties
	) {
		super(properties);
		this.blockEntityFactory = blockEntityFactory;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return blockEntityFactory.apply(blockPos, blockState);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @Nullable <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, BlockState state, BlockEntityType<U> blockEntityType) {
		if (level.isClientSide) {
			return (level_, pos, state_, be) -> {
				if (be instanceof ITickableBE tickableBE) {
					tickableBE.clientTick();
				}
			};
		} else {
			return (level_, pos, state_, be) -> {
				if (be instanceof ITickableBE tickableBE) {
					tickableBE.serverTick();
				}
			};
		}
	}
}
