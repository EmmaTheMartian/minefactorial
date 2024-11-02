package martian.minefactorial.foundation.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class MFItem extends Item {
	private final String[] hoverText;

	public MFItem(Properties properties, String... hoverText) {
		super(properties);
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
