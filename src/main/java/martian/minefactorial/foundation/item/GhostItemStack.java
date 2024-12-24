package martian.minefactorial.foundation.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GhostItemStack implements INBTSerializable<CompoundTag> {
	public static final Codec<GhostItemStack> CODEC = ItemStack.OPTIONAL_CODEC.xmap(GhostItemStack::new, GhostItemStack::getStack);

	public static final GhostItemStack EMPTY = new GhostItemStack((Void) null);

	private ItemStack stack;

	public GhostItemStack(ItemLike item) {
		this(item, 1);
	}

	public GhostItemStack(Holder<Item> tag) {
		this(tag.value(), 1);
	}

	public GhostItemStack(Holder<Item> tag, int count, DataComponentPatch components) {
		this(tag.value(), count);
		this.stack.applyComponentsAndValidate(components);
	}

	public GhostItemStack(Holder<Item> item, int count) {
		this(item.value(), count);
	}

	public GhostItemStack(ItemLike item, int count) {
		this(new ItemStack(item, count));
	}

	private GhostItemStack(ItemStack stack) {
		this.stack = stack;
		this.getItem().verifyComponentsAfterLoad(this.stack);
	}

	private GhostItemStack(@Nullable Void ignore) {
		this.stack = ItemStack.EMPTY;
	}

	public Item getItem() {
		return stack.getItem();
	}

	public int getCount() {
		return stack.getCount();
	}

	public DataComponentMap getComponents() {
		return stack.getComponents();
	}

	public DataComponentPatch getComponentsPatch() {
		return stack.getComponentsPatch();
	}

	public ItemStack getStack() {
		return stack;
	}

	public GhostItemStack copy() {
		return new GhostItemStack(stack.copy());
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		Tag stackTag = this.stack.saveOptional(registries);
		tag.put("Stack", stackTag);
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
		this.stack = ItemStack.parseOptional(registries, tag);
	}

	public static GhostItemStack of(ItemStack stack) {
		return new GhostItemStack(stack.copyWithCount(1));
	}
}
