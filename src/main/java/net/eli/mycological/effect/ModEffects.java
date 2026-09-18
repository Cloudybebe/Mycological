package net.eli.mycological.effect;

import net.eli.mycological.Mycological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    public static final int POISON_DURATION_TICKS = 30 * 20;
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Mycological.MOD_ID);
    public static final DeferredHolder<MobEffect, CordycepualPoisonEffect> CORDYCEPUAL_POISON =
            EFFECTS.register("cordycepual_poison", CordycepualPoisonEffect::new);

    private ModEffects() {}
}
