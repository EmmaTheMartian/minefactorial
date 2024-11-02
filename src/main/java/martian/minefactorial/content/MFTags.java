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

	public static final TagKey<Fluid> MILK = FluidTags.create(c("milk"));
	public static final TagKey<Fluid> STEAM = FluidTags.create(c("steam"));
	public static final TagKey<Fluid> OIL = FluidTags.create(c("oil"));
	public static final TagKey<Fluid> CRUDE_OIL = FluidTags.create(c("crude_oil"));
}
