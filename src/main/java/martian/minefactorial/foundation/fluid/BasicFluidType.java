package martian.minefactorial.foundation.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

// https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Course-121-Module-5/blob/2c56e563d3411f9b585c1f3208653196a977087f/src/main/java/net/kaupenjoe/mccourse/fluid/BaseFluidType.java
public class BasicFluidType extends FluidType {
	private final ResourceLocation stillTexture, flowingTexture, overlayTexture;
	private final int tintColour;
	private final Vector3f fogColour;

	public BasicFluidType(ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlayTexture, int tintColour, Vector3f fogColour, FluidType.Properties properties) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.tintColour = tintColour;
		this.fogColour = fogColour;
	}

	public static IClientFluidTypeExtensions getClientExtensionsFor(BasicFluidType fluidType) {
		return new IClientFluidTypeExtensions() {
			@Override
			public @NotNull ResourceLocation getStillTexture() {
				return fluidType.stillTexture;
			}

			@Override
			public @NotNull ResourceLocation getFlowingTexture() {
				return fluidType.flowingTexture;
			}

			@Override
			public @NotNull ResourceLocation getOverlayTexture() {
				return fluidType.overlayTexture;
			}

			@Override
			public int getTintColor() {
				return fluidType.tintColour;
			}

			@Override
			@ParametersAreNonnullByDefault
			public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColour) {
				return fluidType.fogColour;
			}

			@Override
			@ParametersAreNonnullByDefault
			public void modifyFogRender(Camera camera, FogRenderer.FogMode fogMode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape fogShape) {
				RenderSystem.setShaderFogStart(1f);
				RenderSystem.setShaderFogEnd(6f); // Distance when the fog starts
			}
		};
	}
}
