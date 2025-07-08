package martian.minefactorial.api.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public record EntityIngredient(
        Optional<EntityType<?>> entity,
        Optional<TagKey<EntityType<?>>> tag
) {
    public static final MapCodec<EntityIngredient> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity").forGetter(EntityIngredient::entity),
            TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("tag").forGetter(EntityIngredient::tag)
    ).apply(it, EntityIngredient::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public boolean test(EntityType<?> type) {
        if (entity.isPresent() && tag.isPresent()) {
            throw new RuntimeException("EntityIngredient was provided with both a specific entity type and a tag. Check your recipes!");
        } else if (entity().isEmpty() && tag.isEmpty()) {
            throw new RuntimeException("EntityIngredient was provided with neither a specific entity type nor a tag. Check your recipes!");
        }

        if (entity.isPresent()) {
            return entity.get() == type;
        } else if (tag.isPresent()) {
            return type.is(tag.get());
        }

        return false; // This will never happen
    }

    public static EntityIngredient of(EntityType<?> type) {
        return new EntityIngredient(Optional.ofNullable(type), Optional.empty());
    }

    public static EntityIngredient of(TagKey<EntityType<?>> tag) {
        return new EntityIngredient(Optional.empty(), Optional.of(tag));
    }
}