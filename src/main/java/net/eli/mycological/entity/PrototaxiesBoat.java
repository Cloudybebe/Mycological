package net.eli.mycological.entity;

import net.eli.mycological.block.ModBlocks;
import net.eli.mycological.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class PrototaxiesBoat extends Boat {
    public PrototaxiesBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.PROTOTAXIES_BOAT.get();
    }

    @Override
    public ItemEntity spawnAtLocation(ItemLike item) {
        // Vanilla oak geometry is reused, but fall damage must drop our planks.
        return super.spawnAtLocation(item == Blocks.OAK_PLANKS ? ModBlocks.PROTOTAXIES_PLANKS.get() : item);
    }
}
