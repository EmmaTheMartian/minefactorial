package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.content.menu.ContainerSewageCollector;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

public class BlockSewageCollector extends AbstractBlockWithEntity<BlockSewageCollectorBE> {
    public BlockSewageCollector(Properties properties) {
        super(BlockSewageCollectorBE::new, properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            BlockSewageCollectorBE be = level.getBlockEntity(pos, MFBlockEntityTypes.SEWAGE_COLLECTOR.get()).orElseThrow();
            be.tryToCollectSewage();
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockSewageCollectorBE) {
            player.openMenu(new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return state.getBlock().getName();
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                    return new ContainerSewageCollector(windowId, inventory, pos);
                }
            }, buf -> buf.writeBlockPos(pos));
        }
        return ItemInteractionResult.SUCCESS;
    }
}
