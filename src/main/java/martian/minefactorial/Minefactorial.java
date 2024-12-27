package martian.minefactorial;

import com.mojang.logging.LogUtils;
import martian.minefactorial.client.MinefactorialClient;
import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.Foods;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import org.slf4j.Logger;

@Mod(Minefactorial.MODID)
public class Minefactorial {
	public static final String MODID = "minefactorial";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final RandomSource RANDOM = RandomSource.create();

	public Minefactorial(IEventBus modBus, Dist dist) {
		// The milk-ification
		NeoForgeMod.enableMilkFluid();

		// Add event listeners
		modBus.addListener(MFTabs::addItems);
		modBus.register(MinefactorialListeners.ModBusEvents.class);
		NeoForge.EVENT_BUS.register(MinefactorialListeners.GameBusEvents.class);
		if (dist.isClient()) {
			modBus.register(MinefactorialClient.ModBusEvents.class);
			NeoForge.EVENT_BUS.register(MinefactorialClient.GameBusEvents.class);
		}

		// Registration :D
		MFFluids.REGISTRY.register(modBus);
		MFFluidTypes.REGISTRY.register(modBus);
		MFBlocks.REGISTRY.register(modBus);
		MFBlockEntityTypes.REGISTRY.register(modBus);
		MFDataComponents.REGISTRY.register(modBus);
		MFItems.REGISTRY.register(modBus);
		MFTabs.REGISTRY.register(modBus);
		MFMenuTypes.REGISTRY.register(modBus);
		MFRecipeTypes.REGISTRY.register(modBus);
		MFRecipeSerializers.REGISTRY.register(modBus);

		// Add Straw actions
		MFStrawActions.add(FluidTags.WATER, player -> {
			player.heal(2);
			if (player.isOnFire()) {
				player.extinguishFire();
			}
		});
		MFStrawActions.add(FluidTags.LAVA, player -> player.setRemainingFireTicks(200));
		MFStrawActions.add(Tags.Fluids.MILK, player -> player.removeEffectsCuredBy(EffectCures.MILK));
		MFStrawActions.add(stack -> stack.is(MFTags.Fluids.OIL) || stack.is(MFTags.Fluids.CRUDE_OIL), player -> {
			if (player instanceof ServerPlayer) {
				player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 3));
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 2));
				player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 1));
				player.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 1));
			}
		});
		MFStrawActions.add(Tags.Fluids.EXPERIENCE, player -> player.giveExperiencePoints(100));
		MFStrawActions.add(Tags.Fluids.BEETROOT_SOUP, player -> MFStrawActions.eat(Foods.BEETROOT_SOUP, player));
		MFStrawActions.add(Tags.Fluids.MUSHROOM_STEW, player -> MFStrawActions.eat(Foods.MUSHROOM_STEW, player));
		MFStrawActions.add(Tags.Fluids.SUSPICIOUS_STEW, player -> MFStrawActions.eat(Foods.SUSPICIOUS_STEW, player));
		MFStrawActions.add(Tags.Fluids.RABBIT_STEW, player -> MFStrawActions.eat(Foods.RABBIT_STEW, player));
		MFStrawActions.add(Tags.Fluids.HONEY, player -> MFStrawActions.eat(Foods.HONEY_BOTTLE, player));
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static ResourceLocation id(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
}
