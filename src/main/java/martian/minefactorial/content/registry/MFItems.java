package martian.minefactorial.content.registry;


import martian.minefactorial.Minefactorial;
import martian.minefactorial.content.item.ItemStraw;
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

	public static final DeferredItem<?> STRAW = register("straw", () -> new ItemStraw(new Item.Properties().stacksTo(1)));
}
