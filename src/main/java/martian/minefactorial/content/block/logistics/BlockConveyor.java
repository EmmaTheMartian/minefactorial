package martian.minefactorial.content.block.logistics;

import martian.minefactorial.api.block.AbstractConveyorBlock;

public class BlockConveyor extends AbstractConveyorBlock<BlockConveyorBE> {
	public BlockConveyor(Properties properties) {
		super(BlockConveyorBE::new, properties);
	}
}
