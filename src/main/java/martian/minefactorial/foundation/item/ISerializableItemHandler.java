package martian.minefactorial.foundation.item;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;

public interface ISerializableItemHandler extends IItemHandler, INBTSerializable<CompoundTag> {
}
