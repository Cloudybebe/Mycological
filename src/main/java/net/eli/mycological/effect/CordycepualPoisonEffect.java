package net.eli.mycological.effect;

import net.eli.mycological.Mycological;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class CordycepualPoisonEffect extends MobEffect {
    public CordycepualPoisonEffect() {
        super(MobEffectCategory.HARMFUL, 0xBEBB12);
        addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(
                Mycological.MOD_ID, "cordycepual_poison_health"), AttributeModifier.Operation.ADD_VALUE,
                level -> -2.0 * (level + 1));
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(
                Mycological.MOD_ID, "cordycepual_poison_weakness"), AttributeModifier.Operation.ADD_VALUE,
                level -> -4.0 * (level + 1));
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        clampHealth(entity);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        clampHealth(entity);
        return true;
    }

    private static void clampHealth(LivingEntity entity) {
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
