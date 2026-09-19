package net.eli.mycological.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;

public class CordycepsBambooSaplingBlock extends BambooSaplingBlock {
    public CordycepsBambooSaplingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (direction == Direction.UP && neighborState.is(ModBlocks.CORDYCEPS_BAMBOO)) {
            level.setBlock(pos, ModBlocks.CORDYCEPS_BAMBOO.get().defaultBlockState(), 2);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItemsAccess.bambooItem());
    }

    @Override
    protected void growBamboo(Level level, BlockPos pos) {
        level.setBlock(pos.above(), ModBlocks.CORDYCEPS_BAMBOO.get().defaultBlockState()
                .setValue(CordycepsBambooBlock.LEAVES, BambooLeaves.SMALL), 3);
    }

    /** Keeps the block package independent from registry initialization details. */
    private static final class ModItemsAccess {
        private static net.minecraft.world.level.ItemLike bambooItem() {
            return net.eli.mycological.item.ModItems.CORDYCEPS_BAMBOO.get();
        }
    }
}
