package martian.minefactorial;

import com.mojang.logging.LogUtils;
import martian.minefactorial.client.MinefactorialClient;
import martian.minefactorial.content.MFTags;
import martian.minefactorial.content.registry.*;
import martian.minefactorial.datagen.MFDataGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(Minefactorial.MODID)
public class Minefactorial {
	public static final String MODID = "minefactorial";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Minefactorial(IEventBus modBus, Dist dist) {
		// Add event listeners
		modBus.addListener(MFTabs::addItems);
		modBus.addListener(MFDataGen::onGatherData);
		modBus.register(MinefactorialListeners.ModBusEvents.class);
//		NeoForge.EVENT_BUS.register(MinefactorialListeners.GameBusEvents.class); // There are no events on this *yet*
		if (dist.isClient()) {
			modBus.register(MinefactorialClient.ModBusEvents.class);
			NeoForge.EVENT_BUS.register(MinefactorialClient.GameBusEvents.class);
		}

		// Registration :D
		MFFluids.REGISTRY.register(modBus);
		MFFluidTypes.REGISTRY.register(modBus);
		MFBlocks.REGISTRY.register(modBus);
		MFBlockEntityTypes.REGISTRY.register(modBus);
		MFItems.REGISTRY.register(modBus);
		MFTabs.REGISTRY.register(modBus);
		MFMenuTypes.REGISTRY.register(modBus);

		// Add Straw actions
		MFStrawActions.add(FluidTags.WATER, player -> player.heal(2));
		MFStrawActions.add(FluidTags.LAVA, player -> player.setRemainingFireTicks(200));
		MFStrawActions.add(MFTags.MILK, player -> player.removeEffectsCuredBy(EffectCures.MILK));
		MFStrawActions.add(stack -> stack.is(MFTags.OIL) || stack.is(MFTags.CRUDE_OIL), player -> {
			player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 3));
			player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 2));
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 1));
			player.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 1));
		});
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static ResourceLocation id(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}
}
