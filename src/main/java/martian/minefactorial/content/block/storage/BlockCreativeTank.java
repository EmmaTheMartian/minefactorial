package martian.minefactorial.content.block.storage;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockCreativeTank extends AbstractBlockWithEntity<BlockCreativeTankBE> {
	public BlockCreativeTank(Properties properties) {
		super(BlockCreativeTankBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof BlockCreativeTankBE tank && stack.getItem() instanceof BucketItem bucketItem) {
			FluidStack fluidStack = tank.getTank().getFluid();
			if (bucketItem.content.isSame(Fluids.EMPTY) && fluidStack.getAmount() >= 1000) {
				if (!level.isClientSide) {
					stack.shrink(1);
					player.addItem(new ItemStack(tank.getTank().getFluid().getFluid().getBucket()));
					tank.getTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
				}
				level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
			} else if (tank.getTankCapacity() - fluidStack.getAmount() >= 1000) {
				if (!level.isClientSide) {
					stack.shrink(1);
					player.addItem(Items.BUCKET.getDefaultInstance());
					tank.getTank().fill(new FluidStack(bucketItem.content, 1000), IFluidHandler.FluidAction.EXECUTE);
				}
				level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
			}
			if (!level.isClientSide) {
				tank.setChanged();
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
