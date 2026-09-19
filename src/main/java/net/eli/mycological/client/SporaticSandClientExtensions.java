package net.eli.mycological.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

public final class SporaticSandClientExtensions implements IClientBlockExtensions {
    @Override
    public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
        if (level instanceof ClientLevel clientLevel) {
            for (int i = 0; i < 48; i++) {
                manager.add(new SporaticSporeParticle(clientLevel,
                        pos.getX() + level.random.nextDouble(),
                        pos.getY() + level.random.nextDouble(),
                        pos.getZ() + level.random.nextDouble(), state, pos, true));
            }
        }
        return true;
    }
}
