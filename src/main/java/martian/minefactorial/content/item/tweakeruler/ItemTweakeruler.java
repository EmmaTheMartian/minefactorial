package martian.minefactorial.content.item.tweakeruler;

import martian.minefactorial.client.overlay.scrollmenu.OverlayScrollMenu;
import martian.minefactorial.client.overlay.scrollmenu.ScrollMenu;
import martian.minefactorial.client.overlay.scrollmenu.ScrollMenuEntry;
import martian.minefactorial.content.net.PacketServerboundSetTweakerulerMode;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.foundation.item.MFItem;
import martian.minefactorial.managers.TweakerulerHistoryManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

public class ItemTweakeruler extends MFItem {
	public ItemTweakeruler(Properties properties) {
		super(properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Optional<BlockPos> posComponent = stack.get(MFDataComponents.POS);
		if (posComponent != null && posComponent.isPresent()) {
			tooltipComponents.add(Component.translatable("messages.minefactorial.current_pos",
					Component.literal(posComponent.get().toShortString()).withStyle(ChatFormatting.AQUA)));
		}

		@Nullable TweakerulerMode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
		if (modeComponent != null) {
			tooltipComponents.add(Component.translatable("messages.minefactorial.current_mode",
					Component.literal(modeComponent.getSerializedName()).withStyle(ChatFormatting.AQUA)));
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
			BlockPos pos = context.getClickedPos();

			@Nullable TweakerulerMode modeComponent = context.getItemInHand().get(MFDataComponents.TWEAKERULER_MODE);
			if (modeComponent == TweakerulerMode.PLACE) {
				// Placing atop the clicked face instead of selecting that block
				pos = pos.relative(context.getClickedFace());
			}

			context.getItemInHand().set(MFDataComponents.POS, Optional.of(pos));
		} else {
			BlockPos from = posComponent.get();
			BlockPos to = context.getClickedPos();

			@Nullable TweakerulerMode modeComponent = context.getItemInHand().get(MFDataComponents.TWEAKERULER_MODE);
			if (modeComponent != null) {
				Player player = context.getPlayer();
				if (player == null) {
					return InteractionResult.SUCCESS; // This should never happen
				}
				// Find the non-ruler item
				ItemStack nonRuler = context.getHand() == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();
				// Run the action
				TweakerulerHistory history = TweakerulerHistoryManager.getHistoryFor((ServerPlayer) player);
				int blocksChanged = history.run(context.getLevel(), from, to, context.getItemInHand().copy(), nonRuler.copy(), player, modeComponent);
				// Notify the player with the amount of changed blocks
				player.sendSystemMessage(Component.translatable("messages.minefactorial.blocks_changed",
						Component.literal(String.valueOf(blocksChanged)).withStyle(ChatFormatting.RED)));
			}

			context.getItemInHand().set(MFDataComponents.POS, Optional.empty());
		}
		return InteractionResult.SUCCESS;
	}

	@ParametersAreNonnullByDefault
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);

		if (level.isClientSide()) {
			if (!OverlayScrollMenu.isMenuOpen()) {
				OverlayScrollMenu.pushMenu(getScrollMenu(player, stack));
				return InteractionResultHolder.success(stack);
			} else if (OverlayScrollMenu.getTopMenu() instanceof Menu m) {
				// If the menu is a tweakeruler menu, we can close it this way.
				m.triggerSelect(true);
			}
		}

		return InteractionResultHolder.pass(stack);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (isSelected && level.isClientSide && entity instanceof Player player) {
			@Nullable Optional<BlockPos> posComponent = stack.get(MFDataComponents.POS);
			//noinspection OptionalAssignedToNull
			if (posComponent == null || posComponent.isPresent()) {
				return;
			}

			@Nullable TweakerulerMode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
			if (modeComponent != null) {
				player.displayClientMessage(Component.translatable(
						"messages.minefactorial.current_mode",
						Component.literal(modeComponent.getSerializedName()).withStyle(ChatFormatting.AQUA)
				), true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static ScrollMenu getScrollMenu(Player player, ItemStack stack) {
		@Nullable TweakerulerMode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
		TweakerulerMode currentMode = modeComponent == null ? TweakerulerMode.NONE : modeComponent;

		Menu menu = new Menu();

		for (int i = 0; i < TweakerulerMode.values().length; i++) {
			TweakerulerMode mode = TweakerulerMode.values()[i];

			menu.addEntry(new ScrollMenuEntry(Component.literal(mode.getSerializedName()), () -> {
				PacketDistributor.sendToServer(new PacketServerboundSetTweakerulerMode(mode));
//				player.sendSystemMessage(Component.translatable("messages.minefactorial.set_mode_to",
//						Component.literal(mode.getSerializedName()).withStyle(ChatFormatting.AQUA)));
			}));

			if (mode == currentMode) {
				menu.selection = i;
			}
		}

		return menu;
	}

	// We extend it here so that we can confirm if a scroll menu was made by the tweakeruler or not later on.
	public static class Menu extends ScrollMenu {
	}
}
