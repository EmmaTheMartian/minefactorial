package martian.minefactorial.api.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MFBlockItem extends BlockItem {
    private String[] hoverText = {};
    private String[] longHoverText = {};

    public MFBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public MFBlockItem setHoverText(String... hoverText) {
        this.hoverText = hoverText;
        return this;
    }

    public MFBlockItem setLongHoverText(String... longHoverText) {
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
            if (MFItem.showExtendedTooltip) {
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