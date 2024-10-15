package martian.minefactorial.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMachineGeneratorBE extends AbstractMachineBE implements ITickableBE {
	public AbstractMachineGeneratorBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public int getMaxReceive() {
		return 0;
	}

	@Override
	public void serverTick() {
		super.serverTick();
		this.distributePower();
	}
}
