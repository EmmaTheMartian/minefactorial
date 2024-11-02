package martian.minefactorial.content.block;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.ArgLazy;
import martian.minefactorial.foundation.block.AbstractZonedSingleTankAndInventoryMachineBE;
import martian.minefactorial.foundation.fluid.FluidHelpers;
import martian.minefactorial.foundation.item.MFItemStackHandler;
import martian.minefactorial.foundation.entity.IMixinLivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static martian.minefactorial.Minefactorial.id;

public class BlockMobGrinderBE extends AbstractZonedSingleTankAndInventoryMachineBE {
	public static final int SLOTS = 5;
	public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, id("mob_grinder"));
	public static final ArgLazy<DamageSource, Level> DAMAGE_SOURCE = new ArgLazy<>(level -> new DamageSource(level
			.registryAccess()
			.registryOrThrow(Registries.DAMAGE_TYPE)
			.getHolderOrThrow(DAMAGE_TYPE)
			.getDelegate()));

	public BlockMobGrinderBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.MOB_GRINDER.get(), 4000, SLOTS, pos, blockState);
	}

	@Override
	public int getIdleTime() {
		return 200;
	}

	@Override
	public int getMaxWork() {
		return 40;
	}

	@Override
	public int getWorkZoneRange() {
		return 2;
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockMobGrinder.FACING).getOpposite();
	}

	@Override
	protected boolean validate(FluidStack stack) {
		return stack.is(MFFluids.ESSENCE);
	}

	@Override
	public void serverTick() {
		FluidHelpers.tryDistributeFluid(level, this);
		super.serverTick();
	}

	@Override
	public boolean checkForWork() {
		return getTank().getFluidAmount() < getTankCapacity() &&
				!isInventoryFull() &&
				getFirstEntityInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL).isPresent();
	}

	@Override
	public void doWork() {
		assert level != null;
		ServerLevel serverLevel = (ServerLevel) level;

		Optional<LivingEntity> optionalEntity = getFirstEntityInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL);
		if (optionalEntity.isEmpty()) {
			return;
		}

		LivingEntity entity = optionalEntity.get();

		if (!((Object) entity instanceof IMixinLivingEntity mixinLivingEntity)) {
			return; // This should never happen
		}

		mixinLivingEntity.minefactorial$setShouldSkipDrops(true);
		entity.hurt(DAMAGE_SOURCE.get(level), Float.MAX_VALUE);

		// Item output
		assert level.getServer() != null;
		Collection<ItemStack> items = level.getServer()
				.reloadableRegistries()
				.getLootTable(entity.getLootTable())
				.getRandomItems(new LootParams.Builder(serverLevel).create(LootContextParamSet.builder().build()));
		if (!items.isEmpty()) {
			// Simulate item inserts. If this fails then we won't collect the remainder items
			AtomicBoolean hasStorageForItems = new AtomicBoolean(true);
			items.forEach(item -> {
				ItemStack remainder = MFItemStackHandler.insertItem(getInventory(), item, true);
				if (remainder != ItemStack.EMPTY || isInventoryFull()) {
					hasStorageForItems.set(false);
				}
			});
			if (hasStorageForItems.get()) {
				items.forEach(item -> MFItemStackHandler.insertItem(getInventory(), item, false));
			}
		}

		// Essence output
		if (entity.shouldDropExperience()) {
			int reward = entity.getExperienceReward(serverLevel, null) * 10;
			if (reward > 0) {
				FluidStack stack = new FluidStack(MFFluids.ESSENCE, reward);
				if (getTank().fill(stack, IFluidHandler.FluidAction.SIMULATE) == reward) {
					getTank().fill(stack, IFluidHandler.FluidAction.EXECUTE);
					entity.skipDropExperience();
				}
			}
		}

		this.setChanged();
	}
}
