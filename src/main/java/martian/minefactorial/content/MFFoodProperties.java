package martian.minefactorial.content;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public final class MFFoodProperties {
	private MFFoodProperties() { }

	public static final FoodProperties
			RAW_MEAT_INGOT = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build(),
			COOKED_MEAT_INGOT = new FoodProperties.Builder().nutrition(10).saturationModifier(0.8F).build();
}
