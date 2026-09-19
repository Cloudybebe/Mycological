package net.eli.mycological.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Vanilla cactus behavior with survival checks that recognize this custom cactus. */
public class CordycepsCactusBlock extends CactusBlock {
    public CordycepsCactusBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighbor = level.getBlockState(neighborPos);
            if (neighbor.isSolid() || level.getFluidState(neighborPos).is(FluidTags.LAVA)) {
                return false;
            }
        }

        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        var soilDecision = below.canSustainPlant(level, belowPos, Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        }
        return (below.is(this) || below.is(BlockTags.SAND)) && !level.getBlockState(pos.above()).liquid();
    }
}
