package net.eli.mycological.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class SporaticSandBlock extends BrushableBlock {
    public static final MapCodec<BrushableBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("turns_into").forGetter(BrushableBlock::getTurnsInto),
            propertiesCodec()).apply(instance, SporaticSandBlock::new));

    public SporaticSandBlock(Block turnsInto, Properties properties) {
        super(turnsInto, SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED, properties);
    }

    @Override
    public MapCodec<BrushableBlock> codec() {
        return CODEC;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof BrushableBlockEntity brushable) {
            brushable.checkReset();
        }
        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            // Unlike suspicious sand, retain normal falling-block placement and item drops.
            FallingBlockEntity.fall(level, pos, state);
        }
    }
}
