package com.proctoredgames.tiled.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory {
    // PlayerInventory does not declare getMaxCountPerStack in 1.20.1, so it
    // inherits Inventory's default of 64, which caps player slots below the
    // 96-count small tile stacks. Implementing Inventory here is required for
    // the method to be remapped in production builds; 99 matches the limit
    // vanilla adopted in 1.20.5+.
    @Override
    public int getMaxCountPerStack() {
        return 99;
    }
}
