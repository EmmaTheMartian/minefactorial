package martian.minefactorial.api.fluid;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import top.girlkisser.lazuli.api.fluid.ISerializableFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class MFFluidTank extends FluidTank implements ISerializableFluidHandler {
	public boolean canExtract = true, canReceive = true;

	public MFFluidTank(int capacity) {
		super(capacity);
	}

	public MFFluidTank(int capacity, Predicate<FluidStack> validator) {
		super(capacity, validator);
	}

	public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
		return canReceive ? super.fill(resource, action) : 0;
	}

	public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
		return canExtract ? super.drain(maxDrain, action) : FluidStack.EMPTY;
	}

	public int forceFill(FluidStack resource, IFluidHandler.FluidAction action) {
		return super.fill(resource, action);
	}

	public @NotNull FluidStack forceDrain(int maxDrain, IFluidHandler.FluidAction action) {
		return super.drain(maxDrain, action);
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider lookup) {
		CompoundTag tag = new CompoundTag();
		this.writeToNBT(lookup, tag);
		tag.putBoolean("CanExtract", canExtract);
		tag.putBoolean("CanReceive", canReceive);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider lookup, CompoundTag tag) {
		this.readFromNBT(lookup, tag);

		if (tag.contains("CanExtract")) {
			this.canExtract = tag.getBoolean("CanExtract");
		}

		if (tag.contains("CanReceive")) {
			this.canReceive = tag.getBoolean("CanReceive");
		}
	}
}
