package martian.minefactorial.content.item;

import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.MFFluidTypes;
import martian.minefactorial.foundation.Raycasting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ItemStraw extends Item {
	public static final int DRINK_DURATION = 80;

	public ItemStraw(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
		return DRINK_DURATION;
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
		super.finishUsingItem(stack, level, entityLiving);

		if (entityLiving instanceof Player player) {
			// Get the fluid drank
			BlockHitResult hit = Raycasting.blockRaycast(entityLiving, player.blockInteractionRange(), true);
			if (hit == null) {
				return stack;
			}

			BlockState state = level.getBlockState(hit.getBlockPos());
			FluidState fluidState = state.getFluidState();

			if (fluidState == Fluids.EMPTY.defaultFluidState()) {
				return stack;
			}

			FluidType fluid = fluidState.getFluidType();

			// Apply effects
			if (fluid == Fluids.WATER.getFluidType()) {
				entityLiving.heal(2);
			} else if (fluid == Fluids.LAVA.getFluidType()) {
				entityLiving.setRemainingFireTicks(200);
			} else if (fluid == NeoForgeMod.MILK_TYPE.get()) {
				entityLiving.removeEffectsCuredBy(EffectCures.MILK);
			} else if (fluidState.is(MFTags.STEAM)) {
				entityLiving.setRemainingFireTicks(100);
			} else if (fluidState.is(MFTags.OIL)) {
				entityLiving.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 3));
				entityLiving.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 2));
				entityLiving.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 1));
				entityLiving.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 1));
			}
			// Fallbacks
			else if (fluid.getTemperature() >= 1000) {
				entityLiving.setRemainingFireTicks(200);
			}

			// "Drink" the liquid
			level.setBlockAndUpdate(hit.getBlockPos(), Blocks.AIR.defaultBlockState());

			if (entityLiving instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
				serverPlayer.awardStat(Stats.ITEM_USED.get(this));
			}
		}

		return stack;
	}

	@Override
	public @NotNull SoundEvent getDrinkingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	public @NotNull SoundEvent getEatingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}
