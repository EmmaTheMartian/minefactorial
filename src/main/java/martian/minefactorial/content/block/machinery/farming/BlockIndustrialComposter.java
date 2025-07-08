package martian.minefactorial.content.block.machinery.farming;

import martian.minefactorial.content.menu.ContainerIndustrialComposter;
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
import top.girlkisser.lazuli.api.block.AbstractBlockWithEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockIndustrialComposter extends AbstractBlockWithEntity<BlockIndustrialComposterBE> {
    public BlockIndustrialComposter(Properties properties) {
        super(BlockIndustrialComposterBE::new, properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockIndustrialComposterBE) {
            player.openMenu(new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return state.getBlock().getName();
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                    return new ContainerIndustrialComposter(windowId, inventory, pos);
                }
            }, buf -> buf.writeBlockPos(pos));
        }
        return ItemInteractionResult.SUCCESS;
    }
}
