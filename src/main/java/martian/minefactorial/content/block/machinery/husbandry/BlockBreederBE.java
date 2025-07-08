package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.api.block.AbstractZonedInventoryMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Predicate;

public class BlockBreederBE extends AbstractZonedInventoryMachineBE {
    public static final Predicate<Animal> IS_BREEDABLE = animal -> {
        // Interestingly enough, `age` refers to the breeding cooldown too.
        return !animal.isBaby() && animal.canFallInLove() && animal.getAge() <= 0;
    };

    public static final int SLOTS = 5;

    public BlockBreederBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.BREEDER.get(), SLOTS, pos, blockState);
    }

    public boolean hasFoodFor(Animal animal) {
        for (int i = 0 ; i < this.inventory.getSlots() ; i++) {
            if (animal.isFood(this.inventory.getStackInSlot(i))) {
                return true;
            }
        }

        return false;
    }

    public boolean consumeFoodFor(Animal animal) {
        for (int i = 0 ; i < this.inventory.getSlots() ; i++) {
            if (animal.isFood(this.inventory.getStackInSlot(i))) {
                this.inventory.getStackInSlot(i).shrink(1);
                return true;
            }
        }

        return false;
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
    public Direction getEjectDirection(BlockState state) {
        return state.getValue(BlockMobGrinder.FACING).getOpposite();
    }

    @Override
    public boolean checkForWork(ServerLevel level) {
        return getFirstEntityInWorkZone(Animal.class, IS_BREEDABLE).map(this::hasFoodFor).orElse(false);
    }

    @Override
    public void doWork(ServerLevel level) {
        getFirstEntityInWorkZone(Animal.class, IS_BREEDABLE).ifPresent(animal -> {
            if (consumeFoodFor(animal)) {
                animal.setInLove(null);
                this.setChanged();
            }
        });
    }
}
