package net.eli.mycological.item;

import net.eli.mycological.Mycological;
import net.eli.mycological.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mycological.MOD_ID);
    public static final DeferredItem<BlockItem> LEAF_LAYER = ITEMS.registerSimpleBlockItem(ModBlocks.LEAF_LAYER);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private ModItems() {}
}
