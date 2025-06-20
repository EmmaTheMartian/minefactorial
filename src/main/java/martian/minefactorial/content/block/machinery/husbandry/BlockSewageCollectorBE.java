package martian.minefactorial.content.block.machinery.husbandry;

import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.content.registry.MFFluids;
import martian.minefactorial.foundation.block.AbstractSingleTankBE;
import martian.minefactorial.foundation.block.ITickableBE;
import martian.minefactorial.foundation.fluid.MFFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockSewageCollectorBE extends AbstractSingleTankBE implements ITickableBE {
    public static final int COOLDOWN_TICKS = 50;
    public int cooldownTicks = 0;

    public BlockSewageCollectorBE(BlockPos pos, BlockState blockState) {
        super(MFBlockEntityTypes.SEWAGE_COLLECTOR.get(), 4000, pos, blockState);
    }

    public void tryToCollectSewage() {
        if (cooldownTicks <= 0) {
            ((MFFluidTank) getTank()).forceFill(new FluidStack(MFFluids.SEWAGE, 25), IFluidHandler.FluidAction.EXECUTE);
            cooldownTicks = COOLDOWN_TICKS;
            setChanged();
        }
    }

    @Override
    protected MFFluidTank makeFluidTank() {
        MFFluidTank tank = new MFFluidTank(this.capacity, this::validate) {
            @Override
            public void onContentsChanged() {
                BlockSewageCollectorBE.this.setChanged();
            }
        };
        tank.canReceive = false;
        return tank;
    }

    @Override
    public void serverTick(ServerLevel level) {
        if (getTank().getFluidAmount() > 0) {
            IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().relative(Direction.DOWN), Direction.DOWN);
            if (fluidHandler != null) {
                int amount = Math.min(getTank().getFluidAmount(), getMaxFluidExtract());
                FluidStack stack = getTank().getFluid().copyWithAmount(amount);
                fluidHandler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                ((MFFluidTank) getTank()).forceDrain(amount, IFluidHandler.FluidAction.EXECUTE);
            }
        }

        if (cooldownTicks > 0)
            cooldownTicks--;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("CooldownTicks")) {
            cooldownTicks = tag.getInt("CooldownTicks");
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("CooldownTicks", cooldownTicks);
    }
}
