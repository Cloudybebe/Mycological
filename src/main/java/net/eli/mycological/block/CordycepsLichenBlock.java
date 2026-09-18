package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CordycepsLichenBlock extends GlowLichenBlock {
    public static final MapCodec<GlowLichenBlock> CODEC = simpleCodec(CordycepsLichenBlock::new);

    public CordycepsLichenBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<GlowLichenBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        // Vanilla checks Items.GLOW_LICHEN; use this block's item for adding faces.
        return !context.getItemInHand().is(asItem())
                || Direction.stream().anyMatch(direction -> !state.getValue(MultifaceBlock.getFaceProperty(direction)));
    }
}
