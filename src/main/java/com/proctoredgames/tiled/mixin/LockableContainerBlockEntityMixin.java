package com.proctoredgames.tiled.mixin;

import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LockableContainerBlockEntity.class)
public abstract class LockableContainerBlockEntityMixin implements Inventory {
    // LockableContainerBlockEntity backs container block entities (chests,
    // barrels, furnaces, hoppers, shulker boxes, ...) and does not declare
    // getMaxCountPerStack in 1.20.1, so it inherits Inventory's default of 64
    // and splits 96-count tile stacks moved into it. 99 matches the limit
    // vanilla adopted in 1.20.5+.
    @Override
    public int getMaxCountPerStack() {
        return 99;
    }
}
