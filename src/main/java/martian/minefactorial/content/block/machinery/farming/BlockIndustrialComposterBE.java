package martian.minefactorial.content.block.machinery.farming;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.block.AbstractEnergyBE;
import top.girlkisser.lazuli.api.block.IFluidBE;
import top.girlkisser.lazuli.api.block.ITickableBE;

public class BlockIndustrialComposterBE extends AbstractEnergyBE implements ITickableBE {
    public static final int WORK_COST = 2;

    private final FluidTank sewageTank = new FluidTank(2000, fluidStack -> fluidStack.is(MFFluids.SEWAGE)) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private final FluidTank industrialFertilizerTank = new FluidTank(2000, fluidStack -> fluidStack.is(MFFluids.INDUSTRIAL_FERTILIZER)) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    public BlockIndustrialComposterBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.INDUSTRIAL_COMPOSTER.get(), pos, blockState);
    }

    public FluidTank getSewageTank() {
        return sewageTank;
    }

    public FluidTank getIndustrialFertilizerTank() {
        return industrialFertilizerTank;
    }

    @Override
    public int getMaxEnergyExtract() {
        return 0;
    }

    @Override
    public void serverTick(ServerLevel level) {
        if (industrialFertilizerTank.getFluidAmount() < industrialFertilizerTank.getCapacity() && !sewageTank.isEmpty() && getEnergyStored() >= WORK_COST) {
            getEnergyStorage().forceExtractEnergy(WORK_COST, false);
            sewageTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
            industrialFertilizerTank.fill(new FluidStack(MFFluids.INDUSTRIAL_FERTILIZER, 1), IFluidHandler.FluidAction.EXECUTE);
        }

        IFluidBE.tryPushFluid(this.industrialFertilizerTank, level, 4, this, Direction.UP);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        CompoundTag sewage = new CompoundTag();
        getSewageTank().writeToNBT(registries, sewage);
        tag.put("SewageTank", sewage);

        CompoundTag industrialFertilizer = new CompoundTag();
        getIndustrialFertilizerTank().writeToNBT(registries, industrialFertilizer);
        tag.put("FertilizerTank", industrialFertilizer);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("SewageTank"))
            getSewageTank().readFromNBT(registries, tag.getCompound("SewageTank"));
        if (tag.contains("FertilizerTank"))
            getIndustrialFertilizerTank().readFromNBT(registries, tag.getCompound("FertilizerTank"));
    }

    public IFluidHandler getTankForSide(Direction direction) {
        if (direction == Direction.UP) {
            return industrialFertilizerTank;
        } else {
            return sewageTank;
        }
    }
}
