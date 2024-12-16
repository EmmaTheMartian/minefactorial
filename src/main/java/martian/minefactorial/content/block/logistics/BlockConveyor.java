package martian.minefactorial.content.block.logistics;

import martian.minefactorial.foundation.block.AbstractConveyorBlock;

public class BlockConveyor extends AbstractConveyorBlock<BlockConveyorBE> {
	public BlockConveyor(Properties properties, String... hoverText) {
		super(BlockConveyorBE::new, properties, hoverText);
	}
}
