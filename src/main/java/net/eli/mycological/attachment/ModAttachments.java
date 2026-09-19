package net.eli.mycological.attachment;

import com.mojang.serialization.Codec;
import net.eli.mycological.Mycological;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Mycological.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> MITHRIDATISM_STAGE =
            ATTACHMENTS.register("mithridatism_stage", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.intRange(0, 4))
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }

    private ModAttachments() {}
}
