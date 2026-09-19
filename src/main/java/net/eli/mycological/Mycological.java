package net.eli.mycological;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.eli.mycological.block.ModBlocks;
import net.eli.mycological.attachment.ModAttachments;
import net.eli.mycological.entity.ModEntities;
import net.eli.mycological.effect.ModEffects;
import net.eli.mycological.item.ModItems;
import net.eli.mycological.item.ModCreativeModeTabs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Mycological.MOD_ID)
public class Mycological {
    public static final String MOD_ID = "mycologicalmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Mycological(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(
                ModBlocks.CORDYCEPS_BAMBOO.getId(), ModBlocks.POTTED_CORDYCEPS_BAMBOO));
    }

    // Add leaf litter to the natural blocks tab.
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModItems.LEAF_LAYER);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() == ItemAbilities.AXE_STRIP
                && event.getHeldItemStack().canPerformAction(ItemAbilities.AXE_STRIP)) {
            if (event.getState().is(ModBlocks.PROTOTAXIES_LOG)) {
                event.setFinalState(ModBlocks.PROTOTAXIES_STRIPPED_LOG.get().defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, event.getState().getValue(RotatedPillarBlock.AXIS)));
            } else if (event.getState().is(ModBlocks.PROTOTAXIES_WOOD)) {
                event.setFinalState(ModBlocks.PROTOTAXIES_STRIPPED_WOOD.get().defaultBlockState()
                        .setValue(RotatedPillarBlock.AXIS, event.getState().getValue(RotatedPillarBlock.AXIS)));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        if (!player.level().isClientSide()
                && player.getData(ModAttachments.MITHRIDATISM_STAGE) > 0
                && !player.hasEffect(ModEffects.CORDYCEPUAL_POISON)) {
            player.setData(ModAttachments.MITHRIDATISM_STAGE, 0);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
