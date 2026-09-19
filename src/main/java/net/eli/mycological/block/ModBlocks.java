package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import net.eli.mycological.Mycological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Mycological.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES =
            DeferredRegister.create(Registries.BLOCK_TYPE, Mycological.MOD_ID);
    public static final WoodType PROTOTAXIES_WOOD_TYPE = WoodType.register(
            new WoodType(Mycological.MOD_ID + ":prototaxies", BlockSetType.OAK));

    public static final DeferredBlock<LeafLayerBlock> LEAF_LAYER = BLOCKS.registerBlock(
            "leaf_layer", LeafLayerBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.1F)
                    .sound(SoundType.GRASS)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .noOcclusion());

    public static final DeferredBlock<SnowyDirtBlock> CORDYCEPS_GRASS_BLOCK = BLOCKS.registerBlock(
            "cordyceps_grass_block", SnowyDirtBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK));
    public static final DeferredBlock<CordycepsGrassBlock> CORDYCEPS_GRASS = BLOCKS.registerBlock(
            "cordyceps_grass", CordycepsGrassBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS));
    public static final DeferredBlock<DoublePlantBlock> CORDYCEPS_LARGE_GRASS = BLOCKS.registerBlock(
            "cordyceps_large_grass", DoublePlantBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS));
    public static final DeferredBlock<CordycepsLichenBlock> CORDYCEPS_LICHEN = BLOCKS.registerBlock(
            "cordyceps_lichen", CordycepsLichenBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLOW_LICHEN));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_LOG = BLOCKS.registerBlock(
            "prototaxies_log", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).sound(SoundType.STEM));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_STRIPPED_LOG = BLOCKS.registerBlock(
            "prototaxies_stripped_log", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).sound(SoundType.STEM));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_WOOD = BLOCKS.registerBlock(
            "prototaxies_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.STEM));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_STRIPPED_WOOD = BLOCKS.registerBlock(
            "prototaxies_stripped_wood", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).sound(SoundType.STEM));
    public static final DeferredBlock<Block> PROTOTAXIES_SPORE = BLOCKS.registerSimpleBlock(
            "prototaxies_spore", BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).sound(SoundType.STEM));
    public static final DeferredBlock<PrototaxiesSproutBlock> PROTOTAXIES_SPROUT_SMALL = BLOCKS.registerBlock(
            "prototaxies_sprout_small", PrototaxiesSproutBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS));
    public static final DeferredBlock<Block> PROTOTAXIES_PLANKS = BLOCKS.registerSimpleBlock(
            "prototaxies_planks", BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<DoorBlock> PROTOTAXIES_DOOR = BLOCKS.registerBlock(
            "prototaxies_door", properties -> new DoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR));
    public static final DeferredBlock<TrapDoorBlock> PROTOTAXIES_TRAP_DOOR = BLOCKS.registerBlock(
            "prototaxies_trap_door", properties -> new TrapDoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR));
    public static final DeferredBlock<SporaticSandBlock> SPORATIC_SAND = BLOCKS.registerBlock(
            "sporatic_sand", properties -> new SporaticSandBlock(Blocks.SAND, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND));
    public static final DeferredBlock<SporaticSandBlock> RED_SPORATIC_SAND = BLOCKS.registerBlock(
            "red_sporatic_sand", properties -> new SporaticSandBlock(Blocks.RED_SAND, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND));
    public static final DeferredBlock<Block> CORDYCEPS_STEM = BLOCKS.registerSimpleBlock(
            "cordyceps_stem", BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).sound(SoundType.STEM));
    public static final DeferredBlock<Block> CORDYCEPS_STEM_SPORE = BLOCKS.registerSimpleBlock(
            "cordyceps_stem_spore", BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).sound(SoundType.STEM));

    public static final DeferredBlock<Block> CORDYCEPS_MUSHROOM_BLOCK = BLOCKS.registerSimpleBlock(
            "cordyceps_mushroom_block", BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM));
    public static final DeferredBlock<CordycepsCactusBlock> CORDYCEPS_CACTUS = BLOCKS.registerBlock(
            "cordyceps_cactus", CordycepsCactusBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CACTUS));
    public static final DeferredBlock<CordycepsBambooBlock> CORDYCEPS_BAMBOO = BLOCKS.registerBlock(
            "cordyceps_bamboo", CordycepsBambooBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO));
    public static final DeferredBlock<CordycepsBambooSaplingBlock> CORDYCEPS_BAMBOO_SAPLING = BLOCKS.registerBlock(
            "cordyceps_bamboo_sapling", CordycepsBambooSaplingBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_SAPLING).lootFrom(CORDYCEPS_BAMBOO));
    public static final DeferredBlock<FlowerPotBlock> POTTED_CORDYCEPS_BAMBOO = BLOCKS.registerBlock(
            "potted_cordyceps_bamboo",
            properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CORDYCEPS_BAMBOO, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_BAMBOO));

    public static final DeferredBlock<FenceBlock> PROTOTAXIES_FENCE = BLOCKS.registerBlock(
            "prototaxies_fence", FenceBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE));
    public static final DeferredBlock<FenceGateBlock> PROTOTAXIES_FENCE_GATE = BLOCKS.registerBlock(
            "prototaxies_fence_gate", p -> new FenceGateBlock(PROTOTAXIES_WOOD_TYPE, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE));
    public static final DeferredBlock<PressurePlateBlock> PROTOTAXIES_PRESSURE_PLATE = BLOCKS.registerBlock(
            "prototaxies_pressure_plate", p -> new PressurePlateBlock(BlockSetType.OAK, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE));
    public static final DeferredBlock<ButtonBlock> PROTOTAXIES_BUTTON = BLOCKS.registerBlock(
            "prototaxies_button", p -> new ButtonBlock(BlockSetType.OAK, 30, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON));
    public static final DeferredBlock<StairBlock> PROTOTAXIES_STAIRS = BLOCKS.registerBlock(
            "prototaxies_stairs", p -> new StairBlock(PROTOTAXIES_PLANKS.get().defaultBlockState(), p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS));
    public static final DeferredBlock<SlabBlock> PROTOTAXIES_SLAB = BLOCKS.registerBlock(
            "prototaxies_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB));
    public static final DeferredBlock<StandingSignBlock> PROTOTAXIES_SIGN = BLOCKS.registerBlock(
            "prototaxies_sign", p -> new StandingSignBlock(PROTOTAXIES_WOOD_TYPE, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN));
    public static final DeferredBlock<WallSignBlock> PROTOTAXIES_WALL_SIGN = BLOCKS.registerBlock(
            "prototaxies_wall_sign", p -> new WallSignBlock(PROTOTAXIES_WOOD_TYPE, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(PROTOTAXIES_SIGN));
    public static final DeferredBlock<CeilingHangingSignBlock> PROTOTAXIES_HANGING_SIGN = BLOCKS.registerBlock(
            "prototaxies_hanging_sign", p -> new CeilingHangingSignBlock(PROTOTAXIES_WOOD_TYPE, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN));
    public static final DeferredBlock<WallHangingSignBlock> PROTOTAXIES_WALL_HANGING_SIGN = BLOCKS.registerBlock(
            "prototaxies_wall_hanging_sign", p -> new WallHangingSignBlock(PROTOTAXIES_WOOD_TYPE, p), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(PROTOTAXIES_HANGING_SIGN));

    static {
        BLOCK_TYPES.register("sporatic_sand", () -> SporaticSandBlock.CODEC);
        BLOCK_TYPES.register("leaf_layer", () -> LeafLayerBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_grass", () -> CordycepsGrassBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_lichen", () -> CordycepsLichenBlock.CODEC);
        BLOCK_TYPES.register("prototaxies_sprout_small", () -> PrototaxiesSproutBlock.CODEC);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_TYPES.register(eventBus);
        eventBus.addListener(ModBlocks::addBrushableBlocks);
    }

    private static void addBrushableBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.BRUSHABLE_BLOCK, SPORATIC_SAND.get(), RED_SPORATIC_SAND.get());
        event.modify(BlockEntityType.SIGN, PROTOTAXIES_SIGN.get(), PROTOTAXIES_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, PROTOTAXIES_HANGING_SIGN.get(), PROTOTAXIES_WALL_HANGING_SIGN.get());
    }

    private ModBlocks() {}
}
