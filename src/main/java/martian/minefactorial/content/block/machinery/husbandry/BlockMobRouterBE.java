package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.api.block.AbstractZonedInventoryMachineBE;
import martian.minefactorial.content.item.safarinet.SafariNetData;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.girlkisser.lazuli.api.world.AABBHelpers;

public class BlockMobRouterBE extends AbstractZonedInventoryMachineBE {
    public static final int SLOTS = 1;
    public static final float SPEED = 2f;

    public BlockMobRouterBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.MOB_ROUTER.get(), SLOTS, pos, blockState);
    }

    public boolean hasFilter() {
        return this.getInventory().getStackInSlot(0).has(MFDataComponents.SAFARI_NET_DATA);
    }

    public boolean matchEntity(LivingEntity entity) {
        ItemStack stack = this.getInventory().getStackInSlot(0);
        SafariNetData data = stack.get(MFDataComponents.SAFARI_NET_DATA);
        return data != null && entity.getType() == data.entity();
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
    public boolean shouldEjectItems() {
        return false;
    }

    @Override
    public boolean checkForWork(ServerLevel level) {
        return hasFilter() && getFirstEntityInWorkZone(LivingEntity.class, this::matchEntity).isPresent();
    }

    @Override
    public void doWork(ServerLevel level) {
        // Move entities into the block
        getFirstEntityInWorkZone(LivingEntity.class, this::matchEntity).ifPresent(entity -> {
            routeEntity(entity, this);
            this.setChanged();
        });
    }

    @Override
    public boolean onWorkTick(ServerLevel level) {
        // Move entities to the other side of the block
        level.getEntitiesOfClass(LivingEntity.class, AABBHelpers.ofBlock(worldPosition).inflate(0.25f)).forEach(it -> {
            if (this.matchEntity(it)) {
                it.moveTo(this.getBlockPos().relative(this.getBlockState().getValue(BlockMobRouter.FACING).getOpposite()).getBottomCenter());
            }
        });

        return super.onWorkTick(level);
    }

    public static void routeEntity(LivingEntity entity, BlockEntity be) {
        entity.setDeltaMovement(entity.blockPosition().getBottomCenter().vectorTo(be.getBlockPos().getBottomCenter()).normalize().multiply(SPEED, SPEED, SPEED));
    }
}
