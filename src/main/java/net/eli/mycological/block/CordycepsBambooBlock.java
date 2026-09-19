package net.eli.mycological.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.material.FluidState;

/** Vanilla bamboo behavior with all self-references redirected to the Cordyceps pair. */
public class CordycepsBambooBlock extends BambooStalkBlock {
    public CordycepsBambooBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluid.isEmpty()) {
            return null;
        }

        BlockPos belowPos = context.getClickedPos().below();
        BlockState below = context.getLevel().getBlockState(belowPos);
        var soilDecision = below.canSustainPlant(context.getLevel(), belowPos, Direction.UP, defaultBlockState());
        if (!(soilDecision.isDefault() ? below.is(BlockTags.BAMBOO_PLANTABLE_ON) : soilDecision.isTrue())) {
            return null;
        }
        if (below.is(ModBlocks.CORDYCEPS_BAMBOO_SAPLING)) {
            return defaultBlockState().setValue(AGE, 0);
        }
        if (below.is(this)) {
            return defaultBlockState().setValue(AGE, below.getValue(AGE) > 0 ? 1 : 0);
        }

        BlockState above = context.getLevel().getBlockState(context.getClickedPos().above());
        return above.is(this)
                ? defaultBlockState().setValue(AGE, above.getValue(AGE))
                : ModBlocks.CORDYCEPS_BAMBOO_SAPLING.get().defaultBlockState();
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
        if (direction == Direction.UP && neighborState.is(this)
                && neighborState.getValue(AGE) > state.getValue(AGE)) {
            level.setBlock(pos, state.cycle(AGE), 2);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height) {
        BlockState below = level.getBlockState(pos.below());
        BlockPos twoBelowPos = pos.below(2);
        BlockState twoBelow = level.getBlockState(twoBelowPos);
        BambooLeaves leaves = BambooLeaves.NONE;
        if (height >= 1) {
            if (!below.is(this) || below.getValue(LEAVES) == BambooLeaves.NONE) {
                leaves = BambooLeaves.SMALL;
            } else {
                leaves = BambooLeaves.LARGE;
                if (twoBelow.is(this)) {
                    level.setBlock(pos.below(), below.setValue(LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(twoBelowPos, twoBelow.setValue(LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }

        int age = state.getValue(AGE) != 1 && !twoBelow.is(this) ? 0 : 1;
        int stage = (height < 11 || random.nextFloat() >= 0.25F) && height != 15 ? 0 : 1;
        level.setBlock(pos.above(), defaultBlockState().setValue(AGE, age)
                .setValue(LEAVES, leaves).setValue(STAGE, stage), 3);
    }

    @Override
    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int height = 0;
        while (height < MAX_HEIGHT && level.getBlockState(pos.above(height + 1)).is(this)) {
            height++;
        }
        return height;
    }

    @Override
    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int height = 0;
        while (height < MAX_HEIGHT && level.getBlockState(pos.below(height + 1)).is(this)) {
            height++;
        }
        return height;
    }
}
