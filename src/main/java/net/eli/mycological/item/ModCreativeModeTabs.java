package net.eli.mycological.item;

import net.eli.mycological.Mycological;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mycological.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MYCOLOGICAL = TABS.register(
            "mycological", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mycologicalmod.mycological"))
                    .icon(() -> ModItems.LEAF_LAYER.get().getDefaultInstance())
                    .withTabsBefore(CreativeModeTabs.NATURAL_BLOCKS)
                    .displayItems((parameters, output) ->
                            ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                    .build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    private ModCreativeModeTabs() {}
}
