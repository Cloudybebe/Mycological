package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CordycepsGrassBlock extends TallGrassBlock {
    public static final MapCodec<TallGrassBlock> CODEC = simpleCodec(CordycepsGrassBlock::new);

    public CordycepsGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<TallGrassBlock> codec() {
        return CODEC;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState tallState = ModBlocks.CORDYCEPS_LARGE_GRASS.get().defaultBlockState();
        if (tallState.canSurvive(level, pos) && level.isEmptyBlock(pos.above())) {
            DoublePlantBlock.placeAt(level, tallState, pos, 2);
        }
    }
}
