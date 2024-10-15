package martian.minefactorial.foundation.energy;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface ISerializableEnergyStorage extends IEnergyStorage, INBTSerializable<Tag> {
}
