package martian.minefactorial.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static martian.minefactorial.Minefactorial.id;

public final class MFTags {
	private MFTags() { }

	private static ResourceLocation c(String path) {
		return ResourceLocation.fromNamespaceAndPath("c", path);
	}

	public static final class Items {
		private static TagKey<Item> cTag(String id) {
			return ItemTags.create(c(id));
		}

		private static TagKey<Item> modTag(String id) {
			return ItemTags.create(id(id));
		}

		public static final TagKey<Item>
				RUBBER_INGOT = cTag("ingots/rubber"),
				PLASTIC_INGOT = cTag("ingots/plastic"),
				PLASTIC_SHEETS = cTag("plates/plastic"),
				RULERS = modTag("tools/rulers")
				;
	}

	public static final class Blocks {
		private static TagKey<Block> cTag(String id) {
			return BlockTags.create(c(id));
		}

	}

	public static final class Fluids {
		private static TagKey<Fluid> cTag(String id) {
			return FluidTags.create(c(id));
		}

		public static final TagKey<Fluid>
				STEAM = cTag("steam"),
				OIL = cTag("oil"),
				CRUDE_OIL = cTag("crude_oil"),
				FERTILIZERS = cTag("fertilizers");
	}
}
