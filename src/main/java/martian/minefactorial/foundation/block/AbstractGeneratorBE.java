package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGeneratorBE extends AbstractEnergyBE implements ITickableBE {
	public AbstractGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public int getMaxReceive() {
		return 0;
	}

	@Override
	public void serverTick() {
		this.distributePower();
	}
}
