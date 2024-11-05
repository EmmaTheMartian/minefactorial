package martian.minefactorial.content.registry;

import martian.minefactorial.Minefactorial;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MFTabs {
	private MFTabs() { }

	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minefactorial.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEFACTORIAL_TAB = REGISTRY.register("minefactorial_tab", () -> CreativeModeTab.builder()
			.icon(MFItems.STRAW::toStack)
			.title(Component.translatable("itemGroup.minefactorial.name"))
			.build());

	public static void addItems(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == MINEFACTORIAL_TAB.get()) {
			MFItems.REGISTRY.getEntries().forEach(item -> event.accept(item.get()));
		}
	}
}
