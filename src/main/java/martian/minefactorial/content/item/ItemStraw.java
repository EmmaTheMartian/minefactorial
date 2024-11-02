package martian.minefactorial.content.item;

import martian.minefactorial.content.registry.MFStrawActions;
import martian.minefactorial.foundation.Raycasting;
import martian.minefactorial.foundation.fluid.IStrawAction;
import martian.minefactorial.foundation.item.MFItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ItemStraw extends MFItem {
	protected final int drinkDuration, drinkAmountMb;

	public ItemStraw(int drinkDuration, int drinkAmountMb, Properties properties, String... hoverText) {
		super(properties, hoverText);
		this.drinkDuration = drinkDuration;
		this.drinkAmountMb = drinkAmountMb;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
		return drinkDuration;
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
				IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, hit.getBlockPos(), null);

				// If the fluid handler is null and the fluid state is empty, the player is trying to drink something
				// that is not a fluid. Normal blocks are not as tasty as oil :3
				if (fluidHandler == null) {
					return stack;
				}

				// Drink straight from the tank
				outer: for (int i = 0; i < fluidHandler.getTanks(); i++) {
					FluidStack simulated = fluidHandler.drain(drinkAmountMb, IFluidHandler.FluidAction.SIMULATE);
					if (simulated.is(Fluids.EMPTY) || simulated.getAmount() <= 0) {
						return stack;
					}

					FluidStack drained = fluidHandler.drain(drinkAmountMb, IFluidHandler.FluidAction.EXECUTE);
					// Apply effect, if applicable
					for (IStrawAction action : MFStrawActions.STRAW_ACTIONS) {
						if (action.run(stack, level, player, hit.getBlockPos(), drained)) {
							break outer;
						}
					}
				}
			} else {
				// Apply effect, if applicable
				for (IStrawAction action : MFStrawActions.STRAW_ACTIONS) {
					if (action.run(stack, level, player, hit.getBlockPos(), new FluidStack(fluidState.getType(), 1000))) {
						break;
					}
				}
				// "Drink" the liquid
				level.setBlockAndUpdate(hit.getBlockPos(), Blocks.AIR.defaultBlockState());
			}

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
