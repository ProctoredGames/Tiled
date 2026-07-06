package com.proctoredgames.tiled.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftingInventory.class)
public abstract class CraftingInventoryMixin implements Inventory {
    // CraftingInventory does not declare getMaxCountPerStack in 1.20.1, so
    // crafting grids (crafting table and player inventory) inherit Inventory's
    // default of 64 and split 96-count tile stacks moved into them. 99 matches
    // the limit vanilla adopted in 1.20.5+.
    @Override
    public int getMaxCountPerStack() {
        return 99;
    }
}
