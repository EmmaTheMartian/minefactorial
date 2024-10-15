package martian.minefactorial.content.block;

import martian.minefactorial.content.menu.ContainerSteamBoiler;
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

public class BlockSteamBoiler extends AbstractBlockWithEntity<BlockSteamBoilerBE> {
	public BlockSteamBoiler(Properties properties) {
		super(BlockSteamBoilerBE::new, properties);
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			if (level.getBlockEntity(pos) instanceof BlockSteamBoilerBE) {
				MenuProvider provider = new MenuProvider() {
					@Override
					public @NotNull Component getDisplayName() {
						return Component.literal("Steam Boiler");
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
						return new ContainerSteamBoiler(windowId, inventory, pos);
					}
				};
				player.openMenu(provider, buf -> buf.writeBlockPos(pos));
			}
		}
		return ItemInteractionResult.SUCCESS;
	}
}
