package martian.minefactorial.content.item;

import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.foundation.item.MFItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

public class ItemRuler extends MFItem {
	public ItemRuler(Properties properties, String... hoverText) {
		super(properties, hoverText);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Optional<BlockPos> posComponent = stack.get(MFDataComponents.POS);
		if (posComponent != null && posComponent.isPresent()) {
			tooltipComponents.add(Component.translatable("messages.minefactorial.current_pos", posComponent.get().toShortString()));
		}

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		if (context.getLevel().isClientSide) {
			return InteractionResult.SUCCESS;
		}

		@Nullable Optional<BlockPos> posComponent = context.getItemInHand().get(MFDataComponents.POS);
		//noinspection OptionalAssignedToNull
		if (posComponent == null || posComponent.isEmpty()) {
			context.getItemInHand().set(MFDataComponents.POS, Optional.of(context.getClickedPos()));
		} else {
			context.getItemInHand().set(MFDataComponents.POS, Optional.empty());
		}
		return InteractionResult.SUCCESS;
	}
}
