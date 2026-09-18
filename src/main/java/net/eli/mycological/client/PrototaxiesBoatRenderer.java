package net.eli.mycological.client;

import com.mojang.datafixers.util.Pair;
import net.eli.mycological.Mycological;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public final class PrototaxiesBoatRenderer extends BoatRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            Mycological.MOD_ID, "textures/block/prototaxies_planks.png");

    public PrototaxiesBoatRenderer(EntityRendererProvider.Context context) {
        super(context, false);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return Pair.of(TEXTURE, super.getModelWithLocation(boat).getSecond());
    }
}
