package martian.minefactorial.content.item.safarinet;

import martian.minefactorial.api.item.MFItem;
import martian.minefactorial.content.entity.ThrownSafariNet;
import martian.minefactorial.content.registry.MFDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.girlkisser.lazuli.api.item.LazuliItem;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class ItemSafariNet extends MFItem {
	public ItemSafariNet(Properties properties) {
		super(properties);
	}

	public @Nullable EntityType<?> getType(ItemStack stack) {
		@Nullable SafariNetData data = stack.get(MFDataComponents.SAFARI_NET_DATA);
		return data == null ? null : data.entity();
	}

	public @NotNull Component getName(@NotNull ItemStack stack) {
		@Nullable SafariNetData data = stack.get(MFDataComponents.SAFARI_NET_DATA);
		return data == null ?
				Component.translatable(this.getDescriptionId(stack)) :
				Component.translatable(this.getDescriptionId(stack) + ".captured", Component.translatable(data.entity().getDescriptionId()));
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level instanceof ServerLevel serverLevel) {
			ItemStack stack = context.getItemInHand();
			BlockPos pos = context.getClickedPos();
			Direction direction = context.getClickedFace();
			BlockState state = level.getBlockState(pos);

			BlockPos spawnPos = state.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(direction);

			EntityType<?> type = this.getType(stack);
			if (type == null) {
				return InteractionResult.SUCCESS_NO_ITEM_USED;
			}

			if (type.spawn(
					serverLevel,
					stack,
					context.getPlayer(),
					spawnPos,
					MobSpawnType.SPAWN_EGG,
					true,
					!Objects.equals(pos, spawnPos) && direction == Direction.UP) != null
			) {
				stack.shrink(1);
				level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos);
			}

			return InteractionResult.CONSUME;
		} else {
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
		player.getCooldowns().addCooldown(this, 20);

		if (!level.isClientSide) {
			ThrownSafariNet thrownSafariNet = new ThrownSafariNet(level, player);
			thrownSafariNet.setItem(stack);
			thrownSafariNet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(thrownSafariNet);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		stack.consume(1, player);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}
}
