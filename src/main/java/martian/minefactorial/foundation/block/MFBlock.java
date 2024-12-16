package martian.minefactorial.foundation.block;

import martian.minefactorial.foundation.item.MFBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MFBlock extends Block {
	private final String[] hoverText;

	public MFBlock(Properties properties, String... hoverText) {
		super(properties);
		this.hoverText = hoverText;
	}

	public String[] getHoverText() {
		return hoverText;
	}

	public BlockItem getBlockItem() {
		return new MFBlockItem(this, new Item.Properties(), getHoverText());
	}
}
