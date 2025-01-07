package martian.minefactorial.content.item.safarinet;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import martian.minefactorial.Minefactorial;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record SafariNetData(EntityType<?> entity, CompoundTag entityData) {
	public static MapCodec<SafariNetData> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SafariNetData::entity),
			CompoundTag.CODEC.fieldOf("entityData").forGetter(SafariNetData::entityData)
	).apply(it, SafariNetData::new));
	public static StreamCodec<RegistryFriendlyByteBuf, SafariNetData> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

	public void summon(ServerLevel level, double x, double y, double z) {
		Entity e = entity.create(level);
		if (e == null) {
			Minefactorial.LOGGER.error("SafariNetData#summon had a null entity, this should never happen! Please report it!");
			return;
		}
		e.setPos(x, y, z);
		e.load(entityData);
		level.addFreshEntity(e);
	}
}
