package martian.minefactorial.foundation.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

// https://github.com/McJty/Tut4_3Power/blob/1.20.4_neo/src/main/java/com/mcjty/tutpower/tools/BakedModelHelper.java
public final class BakedModelHelper {
	private BakedModelHelper() { }

	public static BakedQuad quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
		return switch (rotation) {
			case 0 -> quad(v1, v2, v3, v4, sprite);
			case 1 -> quad(v2, v3, v4, v1, sprite);
			case 2 -> quad(v3, v4, v1, v1, sprite);
			case 3 -> quad(v4, v1, v2, v3, sprite);
			default -> quad(v1, v2, v3, v4, sprite);
		};
	}

	public static BakedQuad quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
		Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();
		QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
		builder.setSprite(sprite);
		builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
		putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite);
		putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 1, sprite);
		putVertex(builder, normal, v3.x, v3.y, v3.z, 1, 1, sprite);
		putVertex(builder, normal, v4.x, v4.y, v4.z, 1, 0, sprite);
		return builder.bakeQuad();
	}

	public static void putVertex(VertexConsumer vertexConsumer, Position normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite) {
		vertexConsumer.addVertex((float) x, (float) y, (float) z)
				.setUv(sprite.getU(u), sprite.getV(v))
				.setUv2(0, 0)
				.setColor(0xFFFFFFFF)
				.setNormal((float) normal.x(), (float) normal.y(), (float) normal.z());
	}

	public static Vec3 v(double x, double y, double z) {
		return new Vec3(x, y, z);
	}
}
