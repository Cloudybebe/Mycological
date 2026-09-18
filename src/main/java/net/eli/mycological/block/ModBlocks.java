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
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_SPORE = BLOCKS.registerBlock(
            "prototaxies_spore", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).sound(SoundType.STEM));
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

    static {
        BLOCK_TYPES.register("sporatic_sand", () -> SporaticSandBlock.CODEC);
        BLOCK_TYPES.register("leaf_layer", () -> LeafLayerBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_grass", () -> CordycepsGrassBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_lichen", () -> CordycepsLichenBlock.CODEC);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_TYPES.register(eventBus);
        eventBus.addListener(ModBlocks::addBrushableBlocks);
    }

    private static void addBrushableBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.BRUSHABLE_BLOCK, SPORATIC_SAND.get(), RED_SPORATIC_SAND.get());
    }

    private ModBlocks() {}
}
