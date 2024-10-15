package martian.minefactorial.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public final class MFTags {
	private MFTags() { }

	private static ResourceLocation c(String path) {
		return ResourceLocation.fromNamespaceAndPath("c", path);
	}

	public static final TagKey<Fluid> STEAM = FluidTags.create(c("steam"));
	public static final TagKey<Fluid> OIL = FluidTags.create(c("oil"));
}
