package com.proctoredgames.tiled.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryMixin implements Inventory {
    // SimpleInventory backs most workstation input slots (stonecutter, loom,
    // anvil, grindstone, ...) and does not declare getMaxCountPerStack in
    // 1.20.1, so it inherits Inventory's default of 64 and splits 96-count
    // tile stacks moved into it. 99 matches the limit vanilla adopted in
    // 1.20.5+.
    @Override
    public int getMaxCountPerStack() {
        return 99;
    }
}
