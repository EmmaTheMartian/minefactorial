package martian.minefactorial.content.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.foundation.item.MFItem;
import martian.minefactorial.foundation.world.AABBHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemTweakeruler extends MFItem {
	public ItemTweakeruler(Properties properties, String... hoverText) {
		super(properties, hoverText);
	}

	public ItemTweakeruler(Properties properties, String[] hoverText, String[] longHoverText) {
		super(properties, hoverText, longHoverText);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Optional<BlockPos> posComponent = stack.get(MFDataComponents.POS);
		if (posComponent != null && posComponent.isPresent()) {
			tooltipComponents.add(Component.translatable("messages.minefactorial.current_pos",
					Component.literal(posComponent.get().toShortString()).withStyle(ChatFormatting.AQUA)));
		}

		@Nullable Mode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
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
			context.getItemInHand().set(MFDataComponents.POS, Optional.of(context.getClickedPos()));
		} else {
			BlockPos from = posComponent.get();
			BlockPos to = context.getClickedPos();

			@Nullable Mode modeComponent = context.getItemInHand().get(MFDataComponents.TWEAKERULER_MODE);
			if (modeComponent != null) {
				Player player = context.getPlayer();
				if (player == null) {
					return InteractionResult.SUCCESS; // This should never happen
				}
				ItemStack nonRuler = context.getHand() == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();
				int blocksChanged = modeComponent.run(from, to, context.getItemInHand(), nonRuler, player, context.getLevel());
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

		if (level.isClientSide) {
			return InteractionResultHolder.success(stack);
		}

		if (player.isCrouching()) {
			@Nullable Mode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
			Mode mode = modeComponent == null ?
					Mode.NONE :
					switch (modeComponent) {
						case NONE -> Mode.REPLACE;
						case REPLACE -> Mode.REMOVE;
						case REMOVE -> Mode.NONE;
					};
			stack.set(MFDataComponents.TWEAKERULER_MODE, mode);
			player.sendSystemMessage(Component.translatable("messages.minefactorial.set_mode_to",
					Component.literal(mode.getSerializedName()).withStyle(ChatFormatting.AQUA)));
		}

		return InteractionResultHolder.success(stack);
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

			@Nullable Mode modeComponent = stack.get(MFDataComponents.TWEAKERULER_MODE);
			if (modeComponent != null) {
				player.displayClientMessage(Component.translatable(
						"messages.minefactorial.current_mode",
						Component.literal(modeComponent.getSerializedName()).withStyle(ChatFormatting.AQUA)
				), true);
			}
		}
	}

	@FunctionalInterface
	public interface ITweakerulerAction {
		int run(BlockPos from, BlockPos to, ItemStack ruler, ItemStack other, Player player, Level level);
	}

	public enum Mode implements StringRepresentable, ITweakerulerAction {
		NONE((from, to, ruler, other, player, level) -> 0),
		REPLACE((from, to, ruler, other, player, level) -> {
			Block replaceWith = other.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.AIR;
			AtomicInteger blocksChanged = new AtomicInteger(0);
			BlockPos.betweenClosedStream(from, to).forEach(pos -> {
				level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
				blocksChanged.incrementAndGet();
			});
			return blocksChanged.get();
		}),
		REMOVE((from, to, ruler, ignoredOther, player, level) -> {
			AtomicInteger blocksChanged = new AtomicInteger(0);
			BlockPos.betweenClosedStream(from, to).forEach(pos -> {
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				blocksChanged.incrementAndGet();
			});
			return blocksChanged.get();
		}),
		;

		public static final Codec<Mode> CODEC = StringRepresentable.fromEnum(Mode::values);
		public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

		private final ITweakerulerAction action;

		Mode(ITweakerulerAction action) {
			this.action = action;
		}

		public int run(BlockPos from, BlockPos to, ItemStack ruler, ItemStack other, Player player, Level level) {
			return this.action.run(from, to, ruler, other, player, level);
		}

		@Override
		public @NotNull String getSerializedName() {
			return this.name().toUpperCase();
		}
	}
}
