package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.recipe.RecipeRanching;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFRecipeTypes;
import martian.minefactorial.foundation.block.AbstractZonedSingleTankAndInventoryMachineBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import martian.minefactorial.foundation.fluid.FluidHelpers;
import martian.minefactorial.foundation.fluid.MFFluidTank;
import martian.minefactorial.foundation.item.MFItemStackHandler;
import martian.minefactorial.foundation.recipe.EntityRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static martian.minefactorial.Minefactorial.id;

public class BlockRancherBE extends AbstractZonedSingleTankAndInventoryMachineBE {
	public static final int SLOTS = 5;
	public static final int TANK_CAPACITY = 4000;

	public BlockRancherBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.RANCHER.get(), TANK_CAPACITY, SLOTS, pos, blockState);
	}

	@Override
	protected MFFluidTank makeFluidTank() {
		MFFluidTank tank = new MFFluidTank(this.tankCapacity, this::validateFluidStack) {
			@Override
			public void onContentsChanged() {
				BlockRancherBE.this.setChanged();
			}
		};
		tank.canReceive = false;
		return tank;
	}

	@Override
	public int getIdleTime() {
		return 60;
	}

	@Override
	public int getMaxWork() {
		return 10;
	} //todo: 200

	@Override
	public int getWorkZoneRange() {
		return 2;
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockRancher.FACING).getOpposite();
	}

	@Override
	public void serverTick(ServerLevel level) {
		FluidHelpers.tryPushFluid(getTank(), level, getMaxFluidExtract(), this, Direction.UP);
		super.serverTick(level);
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		return getTank().getFluidAmount() < getTankCapacity() &&
				!isInventoryFull() &&
				getFirstEntityInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL).isPresent();
	}

	@Override
	public void doWork(ServerLevel level) {
		List<LivingEntity> entities = getEntitiesInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL);
		if (entities.isEmpty()) {
			return;
		}

		LivingEntity it = entities.get(Minefactorial.RANDOM.nextInt(entities.size()));
		level.getRecipeManager()
				.getRecipeFor(MFRecipeTypes.RANCHING.get(), new EntityRecipeInput(it.getType()), level)
				.ifPresent(r -> {
					ItemStack item = r.value().rollItemStack();
					if (!item.isEmpty()) {
						ItemStack remainder = MFItemStackHandler.insertItem(getInventory(), item, false);
						if (remainder.isEmpty()) {
							IInventoryBE.ejectStack(level, getBlockPos(), getEjectDirection(getBlockState()), remainder);
						}
					}

					FluidStack fluidOutput = r.value().rollFluidStack();
					if (!fluidOutput.isEmpty() && this.getTank().isFluidValid(fluidOutput)) {
						((MFFluidTank) this.getTank()).forceFill(fluidOutput, IFluidHandler.FluidAction.EXECUTE);
					}
				});

		this.setChanged();
	}
}
