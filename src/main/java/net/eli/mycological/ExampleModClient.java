package net.eli.mycological;

import net.minecraft.client.Minecraft;
import net.eli.mycological.block.ModBlocks;
import net.eli.mycological.client.SporaticSandClientExtensions;
import net.eli.mycological.client.SporaticSporeParticle;
import net.minecraft.core.BlockPos;
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
    static void onClientTick(ClientTickEvent.Post event) {
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
        // Some client setup code
        Mycological.LOGGER.info("HELLO FROM CLIENT SETUP");
        Mycological.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
