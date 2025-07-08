package martian.minefactorial.api.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MFItem extends Item {
    public static boolean showExtendedTooltip = false;

    private String[] hoverText = {};
    private String[] longHoverText = {};

    public MFItem(Properties properties) {
        super(properties);
    }

    public MFItem setHoverText(String... hoverText) {
        this.hoverText = hoverText;
        return this;
    }

    public MFItem setLongHoverText(String... longHoverText) {
        this.longHoverText = longHoverText;
        return this;
    }

    public String[] getHoverText() {
        return hoverText;
    }

    public String[] getLongHoverText() {
        return longHoverText;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        for (String s : hoverText) {
            tooltipComponents.add(Component.translatable(s));
        }

        if (longHoverText.length > 0) {
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