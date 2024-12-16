package martian.minefactorial.foundation.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MFBlockItem extends BlockItem {
	private final String[] hoverText;

	public MFBlockItem(Block block, Properties properties, String... hoverText) {
		super(block, properties);
		this.hoverText = hoverText;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		for (String s : hoverText) {
			tooltipComponents.add(Component.translatable(s));
		}

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}
}
