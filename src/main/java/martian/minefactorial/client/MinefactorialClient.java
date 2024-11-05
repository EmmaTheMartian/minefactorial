package martian.minefactorial.client;

import com.mojang.blaze3d.vertex.PoseStack;
import martian.minefactorial.Minefactorial;
import martian.minefactorial.client.screen.*;
import martian.minefactorial.content.registry.MFFluidTypes;
import martian.minefactorial.content.registry.MFItems;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.Raycasting;
import martian.minefactorial.foundation.block.IZonedBE;
import martian.minefactorial.foundation.fluid.BasicFluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.ApiStatus;

@OnlyIn(Dist.CLIENT)
public final class MinefactorialClient {
	private MinefactorialClient() { }

	@ApiStatus.Internal
	@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class ModBusEvents {
		@SubscribeEvent
		static void registerMenus(final RegisterMenuScreensEvent event) {
			event.register(MFMenuTypes.STEAM_BOILER.get(), ScreenSteamBoiler::new);
			event.register(MFMenuTypes.STEAM_TURBINE.get(), ScreenSteamTurbine::new);
			event.register(MFMenuTypes.CAPACITOR.get(), ScreenCapacitor::new);
			event.register(MFMenuTypes.CREATIVE_CAPACITOR.get(), ScreenCreativeCapacitor::new);
			event.register(MFMenuTypes.BREAKER.get(), ScreenBreaker::new);
			event.register(MFMenuTypes.MOB_GRINDER.get(), ScreenMobGrinder::new);
			event.register(MFMenuTypes.FOUNTAIN.get(), ScreenFountain::new);
			event.register(MFMenuTypes.PUMP.get(), ScreenPump::new);
			event.register(MFMenuTypes.PLACER.get(), ScreenPlacer::new);
		}

		@SubscribeEvent
		static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.STEAM.get()), MFFluidTypes.STEAM);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.OIL.get()), MFFluidTypes.OIL);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.ESSENCE.get()), MFFluidTypes.ESSENCE);
		}
	}

	@ApiStatus.Internal
	@EventBusSubscriber(modid = Minefactorial.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	public static final class GameBusEvents {
		@SubscribeEvent
		static void onRenderWorld(final RenderLevelStageEvent event) {
			if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
				return;

			Player player = Minecraft.getInstance().player;
			Level level = Minecraft.getInstance().level;
			if (player == null || level == null) {
				return;
			}

			if (player.getMainHandItem().is(MFItems.WRENCH) || player.getOffhandItem().is(MFItems.WRENCH)) {
				BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
				if (hit == null) {
					return;
				}

				if (level.getBlockEntity(hit.getBlockPos()) instanceof IZonedBE zonedBE) {
					PoseStack poseStack = event.getPoseStack();
					poseStack.pushPose();
					poseStack.translate(-event.getCamera().getPosition().x, -event.getCamera().getPosition().y, -event.getCamera().getPosition().z);
					LevelRenderer.renderLineBox(event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.LINES), zonedBE.getWorkZone(), 1, 1, 1, 1);
					poseStack.popPose();
				}
			}
		}
	}
}
