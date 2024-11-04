package martian.minefactorial.content.registry;

import martian.minefactorial.foundation.fluid.IStrawAction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

//TODO: Make straw actions server-side only and potentially use a NeoForge data-map?
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
}
