package net.eli.mycological.entity;

import net.eli.mycological.Mycological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Mycological.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<PrototaxiesBoat>> PROTOTAXIES_BOAT = ENTITIES.register(
            "prototaxies_boat", () -> EntityType.Builder.<PrototaxiesBoat>of(PrototaxiesBoat::new, MobCategory.MISC)
                    .sized(1.375F, 0.5625F).eyeHeight(0.5625F).clientTrackingRange(10)
                    .build(Mycological.MOD_ID + ":prototaxies_boat"));

    private ModEntities() {}
}
