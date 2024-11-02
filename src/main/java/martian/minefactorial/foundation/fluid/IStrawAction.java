package martian.minefactorial.foundation.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

@FunctionalInterface
public interface IStrawAction {
	/**
	 * Runs the action. Make sure to check if the fluid matches what it should be!
	 * @param stack The item stack (should be a straw).
	 * @param level The level.
	 * @param player The player slurping the fluid.
	 * @param blockPos The position of the fluid block.
	 * @param fluidStack Mmmm tasty fluid :3
	 * @return If the action actually ran or not.
	 */
	boolean run(ItemStack stack, Level level, Player player, BlockPos blockPos, FluidStack fluidStack);
}
