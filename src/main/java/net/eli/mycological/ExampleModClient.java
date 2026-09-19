package net.eli.mycological;

import net.minecraft.client.Minecraft;
import net.eli.mycological.block.ModBlocks;
import net.eli.mycological.attachment.ModAttachments;
import net.eli.mycological.item.ModItems;
import net.eli.mycological.client.SporaticSandClientExtensions;
import net.eli.mycological.client.SporaticSporeParticle;
import net.eli.mycological.client.PrototaxiesBoatRenderer;
import net.eli.mycological.entity.ModEntities;
import net.eli.mycological.client.CordycepualVines;
import net.eli.mycological.effect.ModEffects;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import java.io.IOException;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Mycological.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Mycological.MOD_ID, value = Dist.CLIENT)
public class ExampleModClient {
    public ExampleModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PROTOTAXIES_BOAT.get(), PrototaxiesBoatRenderer::new);
    }

    @SubscribeEvent
    static void registerShaders(RegisterShadersEvent event) throws IOException {
        CordycepualVines.registerShader(event);
    }

    @SubscribeEvent
    static void registerGuiLayers(RegisterGuiLayersEvent event) {
        CordycepualVines.registerLayer(event);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        CordycepualVines.tick();
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.isPaused() || minecraft.level.getGameTime() % 4 != 0) {
            return;
        }
        for (var player : minecraft.level.players()) {
            double dx = player.getX() - player.xo;
            double dz = player.getZ() - player.zo;
            if (!player.onGround() || player.isSpectator() || dx * dx + dz * dz < 0.0001) {
                continue;
            }
            BlockPos pos = player.getOnPos();
            var state = minecraft.level.getBlockState(pos);
            if (state.is(ModBlocks.SPORATIC_SAND) || state.is(ModBlocks.RED_SPORATIC_SAND)) {
                for (int i = 0; i < 3; i++) {
                    minecraft.particleEngine.add(new SporaticSporeParticle(minecraft.level,
                            player.getX() + (minecraft.level.random.nextDouble() - 0.5) * 0.6,
                            pos.getY() + 1.02,
                            player.getZ() + (minecraft.level.random.nextDouble() - 0.5) * 0.6, state, pos));
                }
            }
        }
    }

    @SubscribeEvent
    static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerBlock(new SporaticSandClientExtensions(),
                ModBlocks.SPORATIC_SAND.get(), ModBlocks.RED_SPORATIC_SAND.get());
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.renderer.Sheets.addWoodType(ModBlocks.PROTOTAXIES_WOOD_TYPE);
            ItemProperties.register(ModItems.TOX_SCREEN.get(),
                    ResourceLocation.fromNamespaceAndPath(Mycological.MOD_ID, "mithridatism_stage"),
                    (stack, level, entity, seed) -> {
                        Player player = entity instanceof Player renderedPlayer
                                ? renderedPlayer : Minecraft.getInstance().player;
                        if (player == null) {
                            return 0.0F;
                        }

                        int stage = player.getData(ModAttachments.MITHRIDATISM_STAGE);
                        if (stage == 4) {
                            var effect = player.getEffect(ModEffects.CORDYCEPUAL_POISON);
                            if (effect == null) {
                                stage = 0;
                            } else if (effect.getDuration() <= 8 * 20) {
                                stage = Math.max(1, (effect.getDuration() - 1) / (2 * 20) + 1);
                            }
                        }
                        return stage / 4.0F;
                    });
        });
        // Some client setup code
        Mycological.LOGGER.info("HELLO FROM CLIENT SETUP");
        Mycological.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
