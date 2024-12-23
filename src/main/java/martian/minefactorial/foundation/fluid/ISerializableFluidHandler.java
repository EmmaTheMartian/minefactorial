package martian.minefactorial.foundation.fluid;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public interface ISerializableFluidHandler extends IFluidHandler, INBTSerializable<CompoundTag> {
}
