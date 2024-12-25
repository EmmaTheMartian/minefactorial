package martian.minefactorial.foundation.item;

import com.mojang.blaze3d.platform.InputConstants;
import martian.minefactorial.client.MFKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MFItem extends Item {
	public static boolean showExtendedTooltip = false;

	private final String[] hoverText;
	private String[] longHoverText = null;

	public MFItem(Properties properties, String... hoverText) {
		super(properties);
		this.hoverText = hoverText;
	}

	public MFItem(Properties properties, String[] hoverText, String[] longHoverText) {
		super(properties);
		this.hoverText = hoverText;
		this.longHoverText = longHoverText;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		for (String s : hoverText) {
			tooltipComponents.add(Component.translatable(s));
		}

		if (longHoverText != null) {
			if (showExtendedTooltip) {
				for (String s : longHoverText) {
					tooltipComponents.add(Component.translatable(s));
				}
			} else {
				tooltipComponents.add(Component.translatable("messages.minefactorial.show_extended_tooltip",
						Component.keybind("key.minefactorial.show_extended_tooltip").withStyle(ChatFormatting.GOLD)
				));
			}
		}

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}
}
