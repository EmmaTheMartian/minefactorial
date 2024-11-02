package martian.minefactorial.content.block;

import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class BlockPlasticTank extends AbstractBlockWithEntity<BlockPlasticTankBE> implements BucketPickup {
	public BlockPlasticTank(Properties properties) {
		super(BlockPlasticTankBE::new, properties);
		this.hasTicker = false;
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof BlockPlasticTankBE tankBE) {
			return new ItemStack(tankBE.getTank().getFluidInTank(0).getFluid().getBucket());
		}
		return ItemStack.EMPTY;
	}

	@Override
	public @NotNull Optional<SoundEvent> getPickupSound() {
		return Optional.empty();
	}
}
