package martian.minefactorial.content.registry;


import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.MFFoodProperties;
import martian.minefactorial.content.item.*;
import martian.minefactorial.content.item.safarinet.ItemSafariNet;
import martian.minefactorial.content.item.tweakeruler.ItemTweakeruler;
import martian.minefactorial.content.item.tweakeruler.TweakerulerMode;
import martian.minefactorial.foundation.item.MFItem;
import martian.regolith.DeferredHolders;
import martian.regolith.RegolithItemUtil;
import martian.regolith.neoforge.RegolithNeoForge;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class MFItems {
	private MFItems() { }

	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Minefactorial.MODID);

	private static DeferredItem<?> register(String id, Supplier<Item> supplier) {
		return REGISTRY.register(id, supplier);
	}

	// Hover ID shorthands
	private static String[] getHoverTextIdsFor(String id, int lines) {
		String[] ids = new String[lines];
		for (int i = 0; i < lines; i++) {
			ids[i] = String.format("item.%s.%s.desc.%d", Minefactorial.MODID, id, i);
		}
		return ids;
	}

	private static String[] getHoverTextIdsFor(String id) {
		return getHoverTextIdsFor(id, 1);
	}

	private static String[] getLongHoverTextIdsFor(String id, int lines) {
		String[] ids = new String[lines];
		for (int i = 0; i < lines; i++) {
			ids[i] = String.format("item.%s.%s.desc.long.%d", Minefactorial.MODID, id, i);
		}
		return ids;
	}

	private static String[] getLongHoverTextIdsFor(String id) {
		return getLongHoverTextIdsFor(id, 1);
	}

	// Registry shorthands
	private static DeferredItem<?> simpleItem(String id) {
		return register(id, () -> new Item(new Item.Properties()));
	}

	private static DeferredItem<?> simpleItem(String id, String[] hoverText) {
		return register(id, () -> new MFItem(new Item.Properties()).setHoverText(hoverText));
	}

	public static final DeferredItem<?>
			// Tools
			STRAW = register("straw", () -> new ItemStraw(80, 1000,
					new Item.Properties().stacksTo(1))
					.setHoverText(getHoverTextIdsFor("straw", 2))),
			MEGA_STRAW = register("mega_straw", () -> new ItemStraw(80, Integer.MAX_VALUE,
					new Item.Properties().stacksTo(1))
					.setHoverText(getHoverTextIdsFor("mega_straw", 3))),
			WRENCH = register("wrench", () -> new ItemWrench(
					new Item.Properties().stacksTo(1))
					.setHoverText(getHoverTextIdsFor("wrench"))),
			SCREWDRIVER = register("screwdriver", () -> new ItemScrewdriver(
					new Item.Properties().stacksTo(1))
					.setHoverText(getHoverTextIdsFor("screwdriver"))),
			RULER = register("ruler", () -> new ItemRuler(
					new Item.Properties()
							.stacksTo(1)
							.component(MFDataComponents.POS, Optional.empty()))
					.setHoverText(getHoverTextIdsFor("ruler"))),
			TWEAKERULER = register("tweakeruler", () -> new ItemTweakeruler(
							new Item.Properties().stacksTo(1)
									.component(MFDataComponents.POS, Optional.empty())
									.component(MFDataComponents.TWEAKERULER_MODE, TweakerulerMode.NONE))
							.setHoverText(getHoverTextIdsFor("tweakeruler", 2))
							.setLongHoverText(getLongHoverTextIdsFor("tweakeruler", 4))),
			TREE_TAP = register("tree_tap", () -> new ItemTreeTap(
					new Item.Properties()
							.stacksTo(1)
							.durability(150))
					.setHoverText(getHoverTextIdsFor("tree_tap"))),
			SAFARI_NET = register("safari_net", () -> new ItemSafariNet(
					new Item.Properties()
							.stacksTo(1)
							.component(DataComponents.ENTITY_DATA, CustomData.EMPTY))
					.setHoverText(getHoverTextIdsFor("safari_net"))),
			// Resources
			RAW_RUBBER = simpleItem("raw_rubber", getHoverTextIdsFor("raw_rubber")),
			RUBBER_INGOT = simpleItem("rubber_ingot"),
			RAW_PLASTIC = simpleItem("raw_plastic"),
			PLASTIC_INGOT = simpleItem("plastic_ingot"),
			PLASTIC_SHEETS = simpleItem("plastic_sheets"),
			RAW_MEAT_INGOT = register("raw_meat_ingot", () -> new MFItem(
					new Item.Properties().food(MFFoodProperties.RAW_MEAT_INGOT))
					.setHoverText(getHoverTextIdsFor("raw_meat_ingot"))),
			COOKED_MEAT_INGOT = register("cooked_meat_ingot", () -> new MFItem(
					new Item.Properties().food(MFFoodProperties.COOKED_MEAT_INGOT))
					.setHoverText(getHoverTextIdsFor("cooked_meat_ingot")))
	;

	// These are used to register macerated ores and ore dusts
	public static final String[] ORE_RESOURCES = {
			"coal", "iron", "copper", "gold", "diamond", "ancient_debris"
	};

	public static final DeferredHolders<Item, DeferredItem<? extends Item>> MACERATED_ORES = RegolithItemUtil.registerItems(
			RegolithNeoForge.wrapItems(REGISTRY),
			new Item.Properties(),
			Arrays.stream(ORE_RESOURCES).map(it -> "macerated_" + it).toArray(String[]::new)
	);

	public static final DeferredHolders<Item, DeferredItem<? extends Item>> ORE_DUSTS = RegolithItemUtil.registerItems(
			RegolithNeoForge.wrapItems(REGISTRY),
			new Item.Properties(),
			Arrays.stream(ORE_RESOURCES).map(it -> it + "_dust").toArray(String[]::new)
	);
}
