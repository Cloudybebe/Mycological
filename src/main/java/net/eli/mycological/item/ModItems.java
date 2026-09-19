package net.eli.mycological.item;

import net.eli.mycological.Mycological;
import net.eli.mycological.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.eli.mycological.effect.ModEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Mycological.MOD_ID);
    public static final DeferredItem<Item> CORDYCEPS_SPORE = ITEMS.register("cordyceps_spore", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F)
                    .alwaysEdible().effect(() -> new MobEffectInstance(ModEffects.CORDYCEPUAL_POISON,
                            ModEffects.POISON_DURATION_TICKS), 1.0F).build())));
    public static final DeferredItem<BlockItem> LEAF_LAYER = ITEMS.registerSimpleBlockItem(ModBlocks.LEAF_LAYER);
    public static final DeferredItem<BlockItem> CORDYCEPS_GRASS_BLOCK = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_GRASS_BLOCK);
    public static final DeferredItem<BlockItem> CORDYCEPS_GRASS = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_GRASS);
    public static final DeferredItem<BlockItem> CORDYCEPS_LARGE_GRASS = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_LARGE_GRASS);
    public static final DeferredItem<BlockItem> CORDYCEPS_LICHEN = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_LICHEN);
    public static final DeferredItem<BlockItem> PROTOTAXIES_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_LOG);
    public static final DeferredItem<BlockItem> PROTOTAXIES_STRIPPED_LOG = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_STRIPPED_LOG);
    public static final DeferredItem<BlockItem> PROTOTAXIES_WOOD = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_WOOD);
    public static final DeferredItem<BlockItem> PROTOTAXIES_STRIPPED_WOOD = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_STRIPPED_WOOD);
    public static final DeferredItem<BlockItem> PROTOTAXIES_SPORE = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_SPORE);
    public static final DeferredItem<BlockItem> PROTOTAXIES_SPROUT_SMALL = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_SPROUT_SMALL);
    public static final DeferredItem<BlockItem> PROTOTAXIES_PLANKS = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_PLANKS);
    public static final DeferredItem<BlockItem> PROTOTAXIES_DOOR = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_DOOR);
    public static final DeferredItem<BlockItem> PROTOTAXIES_TRAP_DOOR = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_TRAP_DOOR);
    public static final DeferredItem<BlockItem> SPORATIC_SAND = ITEMS.registerSimpleBlockItem(ModBlocks.SPORATIC_SAND);
    public static final DeferredItem<BlockItem> RED_SPORATIC_SAND = ITEMS.registerSimpleBlockItem(ModBlocks.RED_SPORATIC_SAND);
    public static final DeferredItem<BlockItem> CORDYCEPS_STEM = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_STEM);
    public static final DeferredItem<BlockItem> CORDYCEPS_STEM_SPORE = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_STEM_SPORE);

    public static final DeferredItem<BlockItem> CORDYCEPS_MUSHROOM_BLOCK = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_MUSHROOM_BLOCK);
    public static final DeferredItem<BlockItem> CORDYCEPS_CACTUS = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_CACTUS);
    public static final DeferredItem<BlockItem> CORDYCEPS_BAMBOO = ITEMS.registerSimpleBlockItem(ModBlocks.CORDYCEPS_BAMBOO);

    public static final DeferredItem<BlockItem> PROTOTAXIES_FENCE = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_FENCE);
    public static final DeferredItem<BlockItem> PROTOTAXIES_FENCE_GATE = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_FENCE_GATE);
    public static final DeferredItem<BlockItem> PROTOTAXIES_PRESSURE_PLATE = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> PROTOTAXIES_BUTTON = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_BUTTON);
    public static final DeferredItem<BlockItem> PROTOTAXIES_STAIRS = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_STAIRS);
    public static final DeferredItem<BlockItem> PROTOTAXIES_SLAB = ITEMS.registerSimpleBlockItem(ModBlocks.PROTOTAXIES_SLAB);
    public static final DeferredItem<SignItem> PROTOTAXIES_SIGN = ITEMS.register("prototaxies_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PROTOTAXIES_SIGN.get(), ModBlocks.PROTOTAXIES_WALL_SIGN.get()));
    public static final DeferredItem<HangingSignItem> PROTOTAXIES_HANGING_SIGN = ITEMS.register("prototaxies_hanging_sign",
            () -> new HangingSignItem(ModBlocks.PROTOTAXIES_HANGING_SIGN.get(), ModBlocks.PROTOTAXIES_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<PrototaxiesBoatItem> PROTOTAXIES_BOAT = ITEMS.register("prototaxies_boat",
            () -> new PrototaxiesBoatItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private ModItems() {}
}
