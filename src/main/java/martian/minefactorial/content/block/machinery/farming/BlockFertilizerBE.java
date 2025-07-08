package martian.minefactorial.content.block.machinery.farming;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.api.block.AbstractZonedSingleTankMachineBE;
import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import top.girlkisser.lazuli.api.world.AABBHelpers;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlockFertilizerBE extends AbstractZonedSingleTankMachineBE {
    public BlockFertilizerBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.FERTILIZER.get(), 4000, pos, blockState);
    }

    @Override
    protected boolean validate(FluidStack stack) {
        return stack.is(MFTags.Fluids.FERTILIZERS);
    }

    @Override
    public int getIdleTime() {
        return 100;
    }

    @Override
    public int getMaxWork() {
        return 10;
    }

    @Override
    public boolean checkForWork(ServerLevel level) {
        if (this.getTank().getFluidAmount() < 100) {
            return false;
        }

        // Check for crops to fertilize
        AtomicBoolean result = new AtomicBoolean(false);
        AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
            if (result.get()) {
                return;
            }
            BlockState state = level.getBlockState(pos);
            if (
                    state.getBlock() instanceof BonemealableBlock bonemealableBlock &&
                    bonemealableBlock.getType() == BonemealableBlock.Type.GROWER &&
                    bonemealableBlock.isValidBonemealTarget(level, pos, state) &&
                    bonemealableBlock.isBonemealSuccess(level, Minefactorial.RANDOM, pos, state)
            ) {
                result.set(true);
            }
        });
        if (result.get()) {
            return true;
        }

        return result.get();
    }

    @Override
    public void doWork(ServerLevel level) {
        // Fertilize crops
        AtomicBoolean done = new AtomicBoolean(false);
        AABBHelpers.properlyBoundedStreamAABB(getCachedWorkZone().get()).forEach(pos -> {
            if (done.get()) {
                return;
            }
            BlockState state = level.getBlockState(pos);
            if (
                    state.getBlock() instanceof BonemealableBlock bonemealableBlock &&
                    bonemealableBlock.getType() == BonemealableBlock.Type.GROWER &&
                    bonemealableBlock.isValidBonemealTarget(level, pos, state) &&
                    bonemealableBlock.isBonemealSuccess(level, Minefactorial.RANDOM, pos, state)
            ) {
                bonemealableBlock.performBonemeal(level, Minefactorial.RANDOM, pos, state);
                getTank().drain(100, IFluidHandler.FluidAction.EXECUTE);
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, 2, 0.25f, 0.25f, 0.25f, 0.3f);
                done.set(true);
            }
        });
    }
}
