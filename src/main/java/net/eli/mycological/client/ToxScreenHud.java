package net.eli.mycological.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import net.eli.mycological.Mycological;
import net.eli.mycological.attachment.ModAttachments;
import net.eli.mycological.effect.ModEffects;
import net.eli.mycological.item.ModItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public final class ToxScreenHud {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            Mycological.MOD_ID, "textures/gui/tox_screen.png");
    private static ShaderInstance shader;

    public static void registerShader(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(Mycological.MOD_ID, "tox_screen_hud"),
                DefaultVertexFormat.POSITION_TEX), loaded -> shader = loaded);
    }

    public static void registerLayer(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(Mycological.MOD_ID, "tox_screen_hud"), ToxScreenHud::render);
    }

    public static int displayedStage(Player player) {
        var effect = player.getEffect(ModEffects.CORDYCEPUAL_POISON);
        if (effect == null) {
            return 0;
        }

        int stage = player.getData(ModAttachments.MITHRIDATISM_STAGE);
        if (stage <= 0) {
            return 0;
        }

        // Only this mod's Mithridatism instance may drive the meter. Keeping the
        // saved progression as a second gate also prevents the penalty instance
        // (amplifier 4) from masquerading as a completed fourth stage.
        stage = Math.min(stage, Math.min(effect.getAmplifier() + 1, 4));
        if (stage == 4) {
            if (effect.getDuration() <= 8 * 20) {
                return Math.max(1, (effect.getDuration() - 1) / (2 * 20) + 1);
            }
        }
        return stage;
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (shader == null || player == null || minecraft.level == null || minecraft.options.hideGui
                || (!player.getMainHandItem().is(ModItems.TOX_SCREEN.get())
                && !player.getOffhandItem().is(ModItems.TOX_SCREEN.get()))) {
            return;
        }

        int stage = displayedStage(player);
        float width = 24.0F;
        float height = 48.0F;
        float x = graphics.guiWidth() - width - 4.0F;
        float y = graphics.guiHeight() - height - 4.0F;

        graphics.flush();
        ShaderInstance previousShader = RenderSystem.getShader();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        try {
            RenderSystem.setShaderTexture(0, GUI_TEXTURE);
            RenderSystem.setShader(() -> shader);
            shader.safeGetUniform("Stage").set((float) stage);
            shader.safeGetUniform("Time").set((minecraft.level.getGameTime()
                    + deltaTracker.getGameTimeDeltaPartialTick(false)) / 20.0F);
            var matrix = graphics.pose().last().pose();
            var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.addVertex(matrix, x, y + height, 0).setUv(0, 1);
            buffer.addVertex(matrix, x + width, y + height, 0).setUv(1, 1);
            buffer.addVertex(matrix, x + width, y, 0).setUv(1, 0);
            buffer.addVertex(matrix, x, y, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } finally {
            RenderSystem.setShader(() -> previousShader);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private ToxScreenHud() {}
}
