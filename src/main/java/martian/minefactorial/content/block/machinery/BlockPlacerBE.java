package martian.minefactorial.content.block.machinery;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.FakePlayerHelpers;
import martian.minefactorial.foundation.block.AbstractSlottedMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

public class BlockPlacerBE extends AbstractSlottedMachineBE {
	public static final int SLOTS = 1;

	public BlockPlacerBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.PLACER.get(), SLOTS, pos, blockState);
	}

	protected Direction getFacing() {
		return getBlockState().getValue(BlockPlacer.FACING);
	}

	@Override
	public boolean shouldEjectItems() {
		return false;
	}

	@Override
	public boolean checkForWork() {
		assert level != null;
		BlockPos pos = worldPosition.relative(getFacing());
		return level.isInWorldBounds(pos) &&
				level.getBlockState(pos).isAir() &&
				!getItem(0).isEmpty() &&
				getItem(0).getItem() instanceof BlockItem;
	}

	@Override
	public void doWork() {
		assert level != null;
		// This will always be a BlockItem thanks to #checkForWork
		ItemStack toPlace = getItem(0);
		// We place using a fake player so that if the block item has specific data to be placed, it'll be used.
		// If we just use BlockItem#getBlock#getDefaultState then we throw away that data, which can (for example)
		// void shulker boxes! By using a fake player, we can place the block using blockItem#place
		// Technically I could also just copy and paste the code that BlockItem#place uses, but a lot of the methods
		// are not public, and I would prefer to avoid access wideners for this when a fake player can do the job
		// perfectly too.
		FakePlayerHelpers.placeBlockItem((ServerLevel) level, worldPosition.relative(getFacing()), toPlace, getFacing());
		toPlace.shrink(1);
		setChanged();
	}

	@Override
	public int getIdleTime() {
		return 50;
	}
}
