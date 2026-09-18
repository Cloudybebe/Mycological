package net.eli.mycological.client;

/** Animation timing independent of Minecraft rendering, including early cures and effect refreshes. */
public final class VineGrowthState {
    private int elapsedTicks;
    private boolean active;
    private float growth;
    private float previousGrowth;

    public void tick(boolean hasEffect, int remainingTicks, boolean infinite) {
        previousGrowth = growth;
        float target = 0.0F;
        if (hasEffect) {
            if (!active) {
                elapsedTicks = 0;
            }
            elapsedTicks = Math.min(elapsedTicks + 1, 400);
            float advancing = elapsedTicks / 400.0F;
            float retreating = infinite ? 1.0F : Math.min(1.0F, Math.max(0, remainingTicks) / 160.0F);
            target = Math.min(advancing, retreating);
        }
        active = hasEffect;
        growth += (target - growth) * 0.08F;
        if (!hasEffect && growth < 0.001F) {
            growth = 0.0F;
        }
    }

    public float interpolatedGrowth(float partialTick) {
        return previousGrowth + (growth - previousGrowth) * Math.clamp(partialTick, 0.0F, 1.0F);
    }

    public void reset() {
        elapsedTicks = 0;
        active = false;
        growth = previousGrowth = 0.0F;
    }
}
