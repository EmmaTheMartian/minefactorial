package martian.minefactorial.foundation.item;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

public class GhostStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
	public static final Codec<NonNullList<GhostItemStack>> LIST_CODEC = NonNullList.codecOf(GhostItemStack.CODEC);

	protected NonNullList<GhostItemStack> stacks;

	public GhostStackHandler() {
		this(1);
	}

	public GhostStackHandler(int slots) {
		this.stacks = NonNullList.withSize(slots, GhostItemStack.EMPTY);
	}

	public GhostStackHandler(Collection<GhostItemStack> stacks) {
		this.stacks = NonNullList.copyOf(stacks);
	}

	public void onContentsChanged(int slot) { }

	public GhostItemStack get(int i) {
		return this.stacks.get(i);
	}

	@Override
	public void setStackInSlot(int i, @NotNull ItemStack stack) {
		this.stacks.set(i, GhostItemStack.of(stack));
		onContentsChanged(i);
	}

	@Override
	public int getSlots() {
		return stacks.size();
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int i) {
		return stacks.get(i).getStack();
	}

	/** Use {@see #setStackInSlot} instead. */
	@Override
	@ApiStatus.Obsolete
	public @NotNull ItemStack insertItem(int i, @NotNull ItemStack stack, boolean simulate) {
		return stack;
	}

	/** Use {@see #setStackInSlot} instead. */
	@Override
	@ApiStatus.Obsolete
	public @NotNull ItemStack extractItem(int i, int maxCount, boolean simulate) {
		if (!simulate) {
			this.setStackInSlot(i, ItemStack.EMPTY);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int i) {
		return 1;
	}

	@Override
	public boolean isItemValid(int i, @NotNull ItemStack arg) {
		return true;
	}

	@Override
	public CompoundTag serializeNBT(@NotNull HolderLookup.Provider registries) {
		DataResult<Tag> result = LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.stacks);
		Tag tag = result.getOrThrow(s -> new RuntimeException("Failed to serialize NBT for GhostStackHandler: " + s));
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.put("Stacks", tag);
		return compoundTag;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void deserializeNBT(@NotNull HolderLookup.Provider registries, CompoundTag tag) {
		DataResult<Pair<NonNullList<GhostItemStack>, Tag>> result = LIST_CODEC.decode(NbtOps.INSTANCE, tag.get("Stacks"));
		this.stacks = result.getOrThrow(s -> new RuntimeException("Failed to deserialize NBT for GhostStackHandler: " + s)).getFirst();
	}
}
