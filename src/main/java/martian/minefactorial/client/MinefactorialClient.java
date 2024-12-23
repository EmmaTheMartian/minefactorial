package martian.minefactorial.client;

import com.mojang.blaze3d.vertex.PoseStack;
import martian.minefactorial.Minefactorial;
import martian.minefactorial.client.screen.*;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.content.registry.MFFluidTypes;
import martian.minefactorial.content.registry.MFItems;
import martian.minefactorial.content.registry.MFMenuTypes;
import martian.minefactorial.foundation.Raycasting;
import martian.minefactorial.foundation.block.IZonedBE;
import martian.minefactorial.foundation.fluid.BasicFluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
			event.register(MFMenuTypes.SMASHER.get(), ScreenSmasher::new);
			event.register(MFMenuTypes.MACERATOR.get(), ScreenMacerator::new);
			event.register(MFMenuTypes.PLANTER.get(), ScreenPlanter::new);
			event.register(MFMenuTypes.HARVESTER.get(), ScreenHarvester::new);
			event.register(MFMenuTypes.SLAUGHTERHOUSE.get(), ScreenSlaughterhouse::new);
			event.register(MFMenuTypes.MEAT_PACKER.get(), ScreenMeatPacker::new);
		}

		@SubscribeEvent
		static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.STEAM.get()), MFFluidTypes.STEAM);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.OIL.get()), MFFluidTypes.OIL);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.ESSENCE.get()), MFFluidTypes.ESSENCE);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.BEETROOT_SOUP.get()), MFFluidTypes.BEETROOT_SOUP);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.MUSHROOM_STEW.get()), MFFluidTypes.MUSHROOM_STEW);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.SUSPICIOUS_STEW.get()), MFFluidTypes.SUSPICIOUS_STEW);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.RABBIT_STEW.get()), MFFluidTypes.RABBIT_STEW);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.HONEY.get()), MFFluidTypes.HONEY);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.PINK_SLIME.get()), MFFluidTypes.PINK_SLIME);
			event.registerFluidType(BasicFluidType.getClientExtensionsFor(MFFluidTypes.MEAT.get()), MFFluidTypes.MEAT);
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

			MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
			Vec3 cameraPos = event.getCamera().getPosition();
			PoseStack poseStack = event.getPoseStack();

			if (player.getMainHandItem().is(MFItems.WRENCH) || player.getOffhandItem().is(MFItems.WRENCH)) {
				BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
				if (hit == null) {
					return;
				}

				if (level.getBlockEntity(hit.getBlockPos()) instanceof IZonedBE zonedBE) {
					poseStack.pushPose();
					poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
					LevelRenderer.renderLineBox(
							event.getPoseStack(),
							Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.LINES),
							zonedBE.getCachedWorkZone().get(),
							1, 1, 1, 1
					);
					poseStack.popPose();
				}
			} else if (player.getMainHandItem().is(MFItems.RULER) || player.getOffhandItem().is(MFItems.RULER)) {
				ItemStack ruler = player.getMainHandItem().is(MFItems.RULER) ? player.getMainHandItem() : player.getOffhandItem();
				@Nullable Optional<BlockPos> posComponent = ruler.get(MFDataComponents.POS);
				//noinspection OptionalAssignedToNull
				if (posComponent == null || posComponent.isEmpty()) {
					return;
				}

				BlockHitResult hit = Raycasting.blockRaycast(player, player.blockInteractionRange(), false);
				if (hit == null) {
					return;
				}

				AABB aabb = AABB.encapsulatingFullBlocks(posComponent.get(), hit.getBlockPos());

				poseStack.pushPose();
				poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
				LevelRenderer.renderLineBox(
						event.getPoseStack(),
						bufferSource.getBuffer(RenderType.LINES),
						aabb,
						1, 1, 1, 1
				);
				poseStack.popPose();

				player.displayClientMessage(Component.translatable(
						"messages.minefactorial.distance_client_message",
						(int)posComponent.get().getCenter().distanceTo(hit.getBlockPos().getCenter()) + 1,
						(int)aabb.getXsize(),
						(int)aabb.getYsize(),
						(int)aabb.getZsize()
				), true);
			}
		}
	}
}
