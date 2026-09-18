package net.eli.mycological.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import net.eli.mycological.Mycological;
import net.eli.mycological.effect.ModEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public final class CordycepualVines {
    private static final VineGrowthState ANIMATION = new VineGrowthState();
    private static ShaderInstance shader;
    private static LocalPlayer trackedPlayer;
    private static ClientLevel trackedLevel;
    private static float animationTicks;

    public static void registerShader(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(Mycological.MOD_ID, "cordycepual_vines"),
                DefaultVertexFormat.POSITION_TEX), loaded -> shader = loaded);
    }

    public static void registerLayer(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS,
                ResourceLocation.fromNamespaceAndPath(Mycological.MOD_ID, "cordycepual_vines"), CordycepualVines::render);
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != trackedPlayer || minecraft.level != trackedLevel) {
            trackedPlayer = minecraft.player;
            trackedLevel = minecraft.level;
            ANIMATION.reset();
            animationTicks = 0;
        }
        if (minecraft.player == null || minecraft.level == null || minecraft.isPaused()) {
            return;
        }
        var effect = minecraft.player.getEffect(ModEffects.CORDYCEPUAL_POISON);
        boolean active = effect != null && minecraft.player.isAlive() && !minecraft.player.isSpectator();
        ANIMATION.tick(active, active ? effect.getDuration() : 0, active && effect.isInfiniteDuration());
        animationTicks = (animationTicks + 1) % 24000;
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        float growth = ANIMATION.interpolatedGrowth(deltaTracker.getGameTimeDeltaPartialTick(false));
        if (shader == null || growth <= 0.001F || minecraft.player == null || minecraft.level == null
                || !minecraft.player.isAlive() || minecraft.player.isSpectator()
                || !minecraft.options.getCameraType().isFirstPerson() || minecraft.options.hideGui) {
            return;
        }
        graphics.flush();
        ShaderInstance previousShader = RenderSystem.getShader();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        try {
            RenderSystem.setShader(() -> shader);
            shader.safeGetUniform("Growth").set(growth);
            shader.safeGetUniform("Time").set(animationTicks / 20.0F);
            shader.safeGetUniform("ScreenSize").set((float) minecraft.getWindow().getWidth(), (float) minecraft.getWindow().getHeight());
            shader.safeGetUniform("Seed").set((float) (minecraft.player.getUUID().hashCode() & 0xFFFF) / 65535.0F);
            var matrix = graphics.pose().last().pose();
            float width = graphics.guiWidth();
            float height = graphics.guiHeight();
            var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.addVertex(matrix, 0, height, 0).setUv(0, 1);
            buffer.addVertex(matrix, width, height, 0).setUv(1, 1);
            buffer.addVertex(matrix, width, 0, 0).setUv(1, 0);
            buffer.addVertex(matrix, 0, 0, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } finally {
            RenderSystem.setShader(() -> previousShader);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private CordycepualVines() {}
}
