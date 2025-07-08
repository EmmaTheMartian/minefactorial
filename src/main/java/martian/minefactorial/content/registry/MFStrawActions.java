package martian.minefactorial.content.registry;

import martian.minefactorial.api.fluid.IStrawAction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class MFStrawActions {
	private MFStrawActions() { }

	public static final List<IStrawAction> STRAW_ACTIONS = new ArrayList<>();

	public static void add(TagKey<Fluid> fluidTag, Consumer<Player> playerConsumer) {
		STRAW_ACTIONS.add(((stack, level, player, blockPos, fluidStack) -> {
			if (fluidStack.is(fluidTag)) {
				playerConsumer.accept(player);
				return true;
			}
			return false;
		}));
	}

	public static void add(FluidType fluidType, Consumer<Player> playerConsumer) {
		STRAW_ACTIONS.add(((stack, level, player, blockPos, fluidStack) -> {
			if (fluidStack.getFluidType() == fluidType) {
				playerConsumer.accept(player);
				return true;
			}
			return false;
		}));
	}

	public static void add(Predicate<FluidStack> predicate, Consumer<Player> playerConsumer) {
		STRAW_ACTIONS.add(((stack, level, player, blockPos, fluidStack) -> {
			if (predicate.test(fluidStack)) {
				playerConsumer.accept(player);
				return true;
			}
			return false;
		}));
	}

	public static void eat(FoodProperties foodProperties, Player player) {
		// Code adapted from Player#eat, LivingEntity#eat, and LivingEntity#addEatEffect
		//noinspection resource
		Level level = player.level();

		if (!level.isClientSide) {
			player.getFoodData().eat(foodProperties.nutrition(), foodProperties.saturation());
			player.awardStat(Stats.ITEM_USED.get(player.getUseItem().getItem()));
			player.playNotifySound(SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1f, 1f + (level.random.nextFloat() - level.random.nextFloat()) * 0.4f);

			foodProperties.effects().forEach(effect -> {
				if (player.getRandom().nextFloat() < effect.probability()) {
					player.addEffect(effect.effect());
				}
			});

			player.gameEvent(GameEvent.EAT);
		}
	}
}
