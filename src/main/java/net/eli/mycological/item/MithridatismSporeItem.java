package net.eli.mycological.item;

import net.eli.mycological.attachment.ModAttachments;
import net.eli.mycological.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class MithridatismSporeItem extends Item {
    private static final int[] STAGE_DURATIONS = {0, 30 * 20, 60 * 20, 120 * 20, 240 * 20};
    private static final int FAILURE_DURATION = 120 * 20;
    private static final int FAILURE_AMPLIFIER = 4;
    private static final int POISON_DURATION = 30 * 20;
    private final int stage;

    public MithridatismSporeItem(int stage, Properties properties) {
        super(properties);
        if (stage < 1 || stage > 4) {
            throw new IllegalArgumentException("Mithridatism stage must be between 1 and 4");
        }
        this.stage = stage;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide() && entity instanceof Player player) {
            int completedStage = player.getData(ModAttachments.MITHRIDATISM_STAGE);
            if (stage == completedStage + 1) {
                player.setData(ModAttachments.MITHRIDATISM_STAGE, stage);
                player.addEffect(new MobEffectInstance(ModEffects.CORDYCEPUAL_POISON,
                        STAGE_DURATIONS[stage], stage - 1));
            } else {
                player.setData(ModAttachments.MITHRIDATISM_STAGE, 0);
                player.addEffect(new MobEffectInstance(ModEffects.CORDYCEPUAL_POISON,
                        FAILURE_DURATION, FAILURE_AMPLIFIER));
                player.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 1));
            }
        }
        return result;
    }
}
