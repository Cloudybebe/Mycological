package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Upright sprout that accepts normal plant soil and Prototaxies trunk surfaces. */
public class PrototaxiesTopSproutBlock extends TallGrassBlock {
    public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(PrototaxiesTopSproutBlock::new);

    public PrototaxiesTopSproutBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<TallGrassBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModBlocks.PROTOTAXIES_SPORE)
                || state.is(ModBlocks.PROTOTAXIES_LOG)
                || state.is(ModBlocks.PROTOTAXIES_STRIPPED_LOG)
                || state.is(ModBlocks.PROTOTAXIES_WOOD)
                || state.is(ModBlocks.PROTOTAXIES_STRIPPED_WOOD)
                || super.mayPlaceOn(state, level, pos);
    }
}
