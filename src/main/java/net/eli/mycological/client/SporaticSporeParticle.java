package net.eli.mycological.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class SporaticSporeParticle extends TerrainParticle {
    public SporaticSporeParticle(ClientLevel level, double x, double y, double z, BlockState state, BlockPos pos) {
        this(level, x, y, z, state, pos, false);
    }

    public SporaticSporeParticle(ClientLevel level, double x, double y, double z, BlockState state, BlockPos pos,
                                 boolean destroyBurst) {
        super(level, x, y, z, 0.0, 0.0, 0.0, state, pos);
        updateSprite(state, pos);
        // Override TerrainParticle's randomized burst velocity and downward gravity.
        if (destroyBurst) {
            double angle = random.nextDouble() * Math.TAU;
            double horizontalSpeed = 0.018 + random.nextDouble() * 0.017;
            xd = Math.cos(angle) * horizontalSpeed;
            yd = 0.018 + random.nextDouble() * 0.024;
            zd = Math.sin(angle) * horizontalSpeed;
        } else {
            xd = (random.nextDouble() - 0.5) * 0.025;
            yd = 0.015 + random.nextDouble() * 0.02;
            zd = (random.nextDouble() - 0.5) * 0.025;
        }
        gravity = -0.015F;
        friction = 0.98F;
        hasPhysics = false;
        lifetime = destroyBurst ? 65 + random.nextInt(40) : 40 + random.nextInt(30);
        quadSize *= 1.1F;
    }
}
