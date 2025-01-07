package martian.minefactorial.foundation.block;

import martian.minefactorial.foundation.item.MFBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class MFBlock extends Block {
	private String[] hoverText = {};
	private String[] longHoverText = {};

	public MFBlock(Properties properties) {
		super(properties);
	}

	public MFBlock setHoverText(String... hoverText) {
		this.hoverText = hoverText;
		return this;
	}

	public MFBlock setLongHoverText(String... longHoverText) {
		this.longHoverText = longHoverText;
		return this;
	}

	public String[] getHoverText() {
		return hoverText;
	}

	public String[] getLongHoverText() {
		return longHoverText;
	}

	public BlockItem getBlockItem() {
		return new MFBlockItem(this, new Item.Properties())
				.setHoverText(getHoverText())
				.setLongHoverText(getLongHoverText());
	}
}
