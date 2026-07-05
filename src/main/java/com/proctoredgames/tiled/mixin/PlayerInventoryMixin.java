package com.proctoredgames.tiled.mixin;

import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    public int getMaxCountPerStack() {
        return 99;
    }
}
