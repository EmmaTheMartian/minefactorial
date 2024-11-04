package martian.minefactorial.content.block.machinery;

import martian.minefactorial.content.menu.ContainerFountain;
import martian.minefactorial.foundation.block.AbstractBlockWithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockFountain extends AbstractBlockWithEntity<BlockFountainBE> {
	public BlockFountain(Properties properties) {
		super(BlockFountainBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockFountainBE) {
			player.openMenu(new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() {
					return Component.literal("Fountain");
				}

				@Override
				public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
					return new ContainerFountain(windowId, inventory, pos);
				}
			}, buf -> buf.writeBlockPos(pos));
		}
		return ItemInteractionResult.SUCCESS;
	}
}
