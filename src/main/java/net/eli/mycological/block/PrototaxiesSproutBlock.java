package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A non-colliding sprout that grows outward from any sturdy block face. */
public class PrototaxiesSproutBlock extends DirectionalBlock {
    public static final MapCodec<PrototaxiesSproutBlock> CODEC = simpleCodec(PrototaxiesSproutBlock::new);

    public PrototaxiesSproutBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public MapCodec<PrototaxiesSproutBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getClickedFace());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        double depth = this == ModBlocks.PROTOTAXIES_SPROUT_LARGE.get() ? 7.0 : 5.0;
        return switch (state.getValue(FACING)) {
            case NORTH -> Block.box(0.0, 0.0, 16.0 - depth, 16.0, 16.0, 16.0);
            case SOUTH -> Block.box(0.0, 0.0, 0.0, 16.0, 16.0, depth);
            case EAST -> Block.box(0.0, 0.0, 0.0, depth, 16.0, 16.0);
            case WEST -> Block.box(16.0 - depth, 0.0, 0.0, 16.0, 16.0, 16.0);
            case UP -> Block.box(0.0, 0.0, 0.0, 16.0, depth, 16.0);
            case DOWN -> Block.box(0.0, 16.0 - depth, 0.0, 16.0, 16.0, 16.0);
        };
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
