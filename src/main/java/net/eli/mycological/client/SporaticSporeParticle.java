package net.eli.mycological.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class SporaticSporeParticle extends TerrainParticle {
    public SporaticSporeParticle(ClientLevel level, double x, double y, double z, BlockState state, BlockPos pos) {
        super(level, x, y, z, 0.0, 0.0, 0.0, state, pos);
        updateSprite(state, pos);
        // Override TerrainParticle's randomized burst velocity and downward gravity.
        xd = (random.nextDouble() - 0.5) * 0.025;
        yd = 0.015 + random.nextDouble() * 0.02;
        zd = (random.nextDouble() - 0.5) * 0.025;
        gravity = -0.015F;
        friction = 0.98F;
        hasPhysics = false;
        lifetime = 40 + random.nextInt(30);
        quadSize *= 0.45F;
    }
}
