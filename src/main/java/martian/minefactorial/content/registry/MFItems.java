package martian.minefactorial.content.registry;


import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.item.ItemStraw;
import martian.minefactorial.content.item.ItemWrench;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MFItems {
	private MFItems() { }

	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(Minefactorial.MODID);

	private static DeferredItem<?> register(String id, Supplier<Item> supplier) {
		return REGISTRY.register(id, supplier);
	}

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

	public static final DeferredItem<?>
			STRAW = register("straw", () -> new ItemStraw(80, 1000, new Item.Properties().stacksTo(1), getHoverTextIdsFor("straw", 2))),
			MEGA_STRAW = register("mega_straw", () -> new ItemStraw(80, Integer.MAX_VALUE, new Item.Properties().stacksTo(1), getHoverTextIdsFor("mega_straw", 3))),
			WRENCH = register("wrench", () -> new ItemWrench(new Item.Properties().stacksTo(1), getHoverTextIdsFor("wrench")))
	;
}
