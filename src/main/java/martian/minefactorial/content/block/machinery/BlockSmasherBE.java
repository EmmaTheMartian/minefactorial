package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluidTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankAndInventoryMachineBE;
import martian.minefactorial.foundation.block.IInventoryBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.Optional;

public class BlockSmasherBE extends AbstractSingleTankAndInventoryMachineBE {
	public static final int INPUT_SLOTS = 1, OUTPUT_SLOTS = 4, TANK_CAPACITY = 4000;
	public static final int MAX_FORTUNE_LEVEL = 3;
	public static final int ESSENCE_MILLIBUCKETS_PER_FORTUNE_LEVEL = 100;

	public int fortuneLevel = 0;

	public BlockSmasherBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.SMASHER.get(), TANK_CAPACITY, INPUT_SLOTS + OUTPUT_SLOTS, pos, blockState);
	}

	public int getEssenceCost() {
		return fortuneLevel * ESSENCE_MILLIBUCKETS_PER_FORTUNE_LEVEL;
	}

	public ItemStack getPickaxe(HolderLookup.Provider registries) {
		Optional<Holder.Reference<Enchantment>> holder = registries.holder(Enchantments.FORTUNE);
		if (holder.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = Items.DIAMOND_PICKAXE.getDefaultInstance();
		stack.enchant(holder.get(), fortuneLevel);
		return stack;
	}

	@Override
	protected boolean validateFluidStack(FluidStack stack) {
		return stack.is(MFFluidTypes.ESSENCE.get());
	}

	@Override
	public int getMaxWork() {
		return 60;
	}

	@Override
	public int getIdleTime() {
		return 40;
	}

	@Override
	public boolean canEjectSlot(int slot) {
		return slot != 0; // 0 is the input slot
	}

	@Override
	public Direction getEjectDirection(BlockState state) {
		return state.getValue(BlockSmasher.FACING).getOpposite();
	}

	@Override
	public boolean checkForWork(ServerLevel level) {
		return getInventory().getStackInSlot(0).getItem() instanceof BlockItem blockItem &&
				getTank().getFluidAmount() >= getEssenceCost() &&
				blockItem.getBlock().getLootTable() != BuiltInLootTables.EMPTY;
	}

	@Override
	public void doWork(ServerLevel level) {
		ItemStack toSmash = getInventory().getStackInSlot(0);
		Block block = ((BlockItem) toSmash.getItem()).getBlock();

		// Consume essence and item
		int cost = getEssenceCost();
		FluidStack drained = getTank().drain(cost, IFluidHandler.FluidAction.EXECUTE);
		if (drained.getAmount() != cost) {
			return; // This hopefully should never happen, but we want to be certain.
		}

		toSmash.shrink(1);

		// The fun part, smashing stuff!!
		level.getServer()
				.reloadableRegistries()
				.getLootTable(block.getLootTable())
				.getRandomItems(new LootParams.Builder(level)
						.withLuck(fortuneLevel)
						.withParameter(LootContextParams.BLOCK_STATE, block.defaultBlockState())
						.withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
						.withParameter(LootContextParams.TOOL, getPickaxe(level.registryAccess()))
						.create(LootContextParamSets.BLOCK))
				.forEach(stack -> {
					// Insert the item into the smasher's inventory
					ItemStack remainder = IInventoryBE.insertItemInto(getInventory(), stack, 1, getInventory().getSlots());
					// If we failed to insert everything, we will eject the excess items.
					if (!remainder.isEmpty()) {
						IInventoryBE.ejectStack(level, getBlockPos(), getBlockState().getValue(HorizontalDirectionalBlock.FACING), remainder);
						// If there is a remainder then we failed both to insert and to eject. This should
						// basically never happen to a player who is actually using the smasher, so we can
						// just... void it
					}
				});
	}
}
