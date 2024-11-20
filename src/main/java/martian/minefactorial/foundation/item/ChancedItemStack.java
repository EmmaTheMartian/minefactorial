package martian.minefactorial.foundation.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.minefactorial.Minefactorial;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public record ChancedItemStack(ItemStack stack, float chance) {
	public static final Codec<ChancedItemStack> CODEC = RecordCodecBuilder.create(it -> it.group(
			ItemStack.CODEC.fieldOf("stack").forGetter(ChancedItemStack::stack),
			Codec.FLOAT.fieldOf("chance").forGetter(ChancedItemStack::chance)
	).apply(it, ChancedItemStack::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ChancedItemStack> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public ChancedItemStack(ItemLike item, float chance) {
		this(item.asItem().getDefaultInstance(), chance);
	}

	public ChancedItemStack(ItemLike item, int count, float chance) {
		this(new ItemStack(item.asItem(), count), chance);
	}

	public ItemStack roll() {
		return Minefactorial.RANDOM.nextFloat() <= chance ? stack : ItemStack.EMPTY;
	}
}
