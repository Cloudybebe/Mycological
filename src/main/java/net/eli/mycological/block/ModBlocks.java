package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import net.eli.mycological.Mycological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
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
            "prototaxies_log", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_STRIPPED_LOG = BLOCKS.registerBlock(
            "prototaxies_stripped_log", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG));
    public static final DeferredBlock<RotatedPillarBlock> PROTOTAXIES_SPORE = BLOCKS.registerBlock(
            "prototaxies_spore", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM));
    public static final DeferredBlock<Block> PROTOTAXIES_PLANKS = BLOCKS.registerSimpleBlock(
            "prototaxies_planks", BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<DoorBlock> PROTOTAXIES_DOOR = BLOCKS.registerBlock(
            "prototaxies_door", properties -> new DoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR));
    public static final DeferredBlock<TrapDoorBlock> PROTOTAXIES_TRAP_DOOR = BLOCKS.registerBlock(
            "prototaxies_trap_door", properties -> new TrapDoorBlock(BlockSetType.OAK, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR));
    public static final DeferredBlock<ColoredFallingBlock> SPORATIC_SAND = BLOCKS.registerBlock(
            "sporatic_sand", properties -> new ColoredFallingBlock(new ColorRGBA(0xD5AD83), properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND));
    public static final DeferredBlock<ColoredFallingBlock> RED_SPORATIC_SAND = BLOCKS.registerBlock(
            "red_sporatic_sand", properties -> new ColoredFallingBlock(new ColorRGBA(0xB58D6D), properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND));
    public static final DeferredBlock<Block> CORDYCEPS_SHELF = BLOCKS.registerSimpleBlock(
            "cordyceps_shelf", BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM_BLOCK));
    public static final DeferredBlock<Block> CORDYCEPS_SHELF_SPORE = BLOCKS.registerSimpleBlock(
            "cordyceps_shelf_spore", BlockBehaviour.Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK));

    static {
        BLOCK_TYPES.register("leaf_layer", () -> LeafLayerBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_grass", () -> CordycepsGrassBlock.CODEC);
        BLOCK_TYPES.register("cordyceps_lichen", () -> CordycepsLichenBlock.CODEC);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_TYPES.register(eventBus);
    }

    private ModBlocks() {}
}
