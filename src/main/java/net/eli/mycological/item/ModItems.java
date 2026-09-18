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
    public static final DeferredItem<BlockItem> CORDYCEPS_GRASS_BLOCK = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_GRASS_BLOCK);
    public static final DeferredItem<BlockItem> CORDYCEPS_GRASS = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_GRASS);
    public static final DeferredItem<BlockItem> CORDYCEPS_LARGE_GRASS = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_LARGE_GRASS);
    public static final DeferredItem<BlockItem> CORDYCEPS_LICHEN = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_LICHEN);
    public static final DeferredItem<BlockItem> PROTOTAXIES_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_LOG);
    public static final DeferredItem<BlockItem> PROTOTAXIES_STRIPPED_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_STRIPPED_LOG);
    public static final DeferredItem<BlockItem> PROTOTAXIES_SPORE = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_SPORE);
    public static final DeferredItem<BlockItem> PROTOTAXIES_PLANKS = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_PLANKS);
    public static final DeferredItem<BlockItem> PROTOTAXIES_DOOR = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_DOOR);
    public static final DeferredItem<BlockItem> PROTOTAXIES_TRAP_DOOR = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_TRAP_DOOR);
    public static final DeferredItem<BlockItem> SPORATIC_SAND = ITEMS.registerSimpleBlockItem(ModBlocks.SPORATIC_SAND);
    public static final DeferredItem<BlockItem> RED_SPORATIC_SAND = ITEMS.registerSimpleBlockItem(ModBlocks.RED_SPORATIC_SAND);
    public static final DeferredItem<BlockItem> CORDYCEPS_SHELF = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_SHELF);
    public static final DeferredItem<BlockItem> CORDYCEPS_SHELF_SPORE = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_SHELF_SPORE);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private ModItems() {}
}
