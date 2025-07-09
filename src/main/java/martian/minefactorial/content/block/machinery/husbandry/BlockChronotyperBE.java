package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.api.block.AbstractZonedInventoryMachineBE;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.world.AABBHelpers;

public class BlockChronotyperBE extends AbstractZonedInventoryMachineBE {
    public MoveState state = MoveState.BABIES;

    public BlockChronotyperBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.CHRONOTYPER.get(), 0, pos, blockState);
    }

    public boolean matchEntity(Animal entity) {
        return switch (state) {
            case BABIES -> entity.isBaby();
            case ADULTS -> !entity.isBaby();
        };
    }

    @Override
    public int getIdleTime() {
        return 100;
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
        return getFirstEntityInWorkZone(Animal.class, this::matchEntity).isPresent();
    }

    @Override
    public void doWork(ServerLevel level) {
        // Move entities into the block
        getFirstEntityInWorkZone(Animal.class, this::matchEntity).ifPresent(entity -> {
            BlockMobRouterBE.routeEntity(entity, this);
            this.setChanged();
        });
    }

    @Override
    public boolean onWorkTick(ServerLevel level) {
        // Move entities to the other side of the block
        level.getEntitiesOfClass(Animal.class, AABBHelpers.ofBlock(worldPosition).inflate(0.25f)).forEach(it -> {
            if (this.matchEntity(it)) {
                it.moveTo(this.getBlockPos().relative(this.getBlockState().getValue(BlockChronotyper.FACING).getOpposite()).getBottomCenter());
            }
        });

        return super.onWorkTick(level);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("ChronotypeState", state.serialized());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.state = tag.getBoolean("ChronotypeState") ? MoveState.BABIES : MoveState.ADULTS;
    }

    public enum MoveState {
        BABIES,
        ADULTS;

        public boolean serialized() {
            return this == BABIES;
        }
    }
}
