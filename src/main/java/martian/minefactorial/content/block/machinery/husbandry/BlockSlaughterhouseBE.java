package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.ArgLazy;
import martian.minefactorial.foundation.block.AbstractZonedInventoryMachineBE;
import martian.minefactorial.foundation.entity.IMixinLivingEntity;
import martian.minefactorial.foundation.fluid.FluidHelpers;
import martian.minefactorial.foundation.fluid.MFFluidTank;
import martian.minefactorial.foundation.item.MFItemStackHandler;
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
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static martian.minefactorial.Minefactorial.id;

public class BlockSlaughterhouseBE extends AbstractZonedInventoryMachineBE {
	public static final int SLOTS = 5;
	public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, id("slaughterhouse"));
	public static final ArgLazy<DamageSource, Level> DAMAGE_SOURCE = new ArgLazy<>(level -> new DamageSource(level
			.registryAccess()
			.registryOrThrow(Registries.DAMAGE_TYPE)
			.getHolderOrThrow(DAMAGE_TYPE)
			.getDelegate()));

	private final MFFluidTank pinkSlimeTank;
	private final MFFluidTank meatTank;

	public BlockSlaughterhouseBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.SLAUGHTERHOUSE.get(), SLOTS, pos, blockState);

		this.pinkSlimeTank = new MFFluidTank(4000, fluidStack -> fluidStack.is(MFFluids.PINK_SLIME)) {
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		this.pinkSlimeTank.canReceive = false;

		this.meatTank = new MFFluidTank(4000, fluidStack -> fluidStack.is(MFFluids.MEAT)) {
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		this.meatTank.canReceive = false;
	}

	public MFFluidTank getPinkSlimeTank() {
		return pinkSlimeTank;
	}

	public MFFluidTank getMeatTank() {
		return meatTank;
	}

	public @Nullable IFluidHandler getTank(@Nullable Direction direction) {
		Direction facing = getBlockState().getValue(BlockMobGrinder.FACING);
		return direction == facing.getClockWise() ? getPinkSlimeTank() :
				direction == facing.getCounterClockWise() ? getMeatTank() :
				null;
	}

	private int getMaxFluidExtract() {
		return 32;
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
	public void serverTick(ServerLevel level) {
		Direction facing = getBlockState().getValue(BlockMobGrinder.FACING);
		FluidHelpers.tryPushFluid(getPinkSlimeTank(), level, getMaxFluidExtract(), this, facing.getClockWise());
		FluidHelpers.tryPushFluid(getMeatTank(), level, getMaxFluidExtract(), this, facing.getCounterClockWise());
		super.serverTick(level);
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		return getMeatTank().getFluidAmount() < getMeatTank().getCapacity() &&
				getPinkSlimeTank().getFluidAmount() < getPinkSlimeTank().getCapacity() &&
				!isInventoryFull() &&
				getFirstEntityInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL).isPresent();
	}

	@Override
	public void doWork(ServerLevel level) {
		Optional<LivingEntity> optionalEntity = getFirstEntityInWorkZone(LivingEntity.class, IS_ADULT_ANIMAL);
		if (optionalEntity.isEmpty()) {
			return;
		}

		LivingEntity entity = optionalEntity.get();

		if (!((Object) entity instanceof IMixinLivingEntity mixinLivingEntity)) {
			return; // This should never happen
		}

		mixinLivingEntity.minefactorial$setShouldSkipDrops(true);
		entity.skipDropExperience();
		entity.hurt(DAMAGE_SOURCE.get(level), Float.MAX_VALUE);

		// Item output
		Collection<ItemStack> items = level.getServer()
				.reloadableRegistries()
				.getLootTable(entity.getLootTable())
				.getRandomItems(new LootParams.Builder(level).create(LootContextParamSet.builder().build()));
		AtomicInteger meatItems = new AtomicInteger(0);
		if (!items.isEmpty()) {
			// Simulate item inserts. If this fails then we won't collect the remainder items
			AtomicBoolean hasStorageForItems = new AtomicBoolean(true);
			items.forEach(item -> {
				if (item.is(Tags.Items.FOODS_RAW_MEAT)) {
					return;
				}
				ItemStack remainder = MFItemStackHandler.insertItem(getInventory(), item, true);
				if (remainder != ItemStack.EMPTY || isInventoryFull()) {
					hasStorageForItems.set(false);
				}
			});
			if (hasStorageForItems.get()) {
				items.forEach(item -> {
					if (item.is(Tags.Items.FOODS_RAW_MEAT)) {
						meatItems.incrementAndGet();
						return;
					}
					MFItemStackHandler.insertItem(getInventory(), item, false);
				});
			}
		}

		// Pink slime and meat output
		FluidStack pinkSlime = new FluidStack(MFFluids.PINK_SLIME, 100);
		FluidStack meat = new FluidStack(MFFluids.MEAT, meatItems.get() * 200);
		this.getPinkSlimeTank().forceFill(pinkSlime, IFluidHandler.FluidAction.EXECUTE);
		this.getMeatTank().forceFill(meat, IFluidHandler.FluidAction.EXECUTE);

		this.setChanged();
	}
}
