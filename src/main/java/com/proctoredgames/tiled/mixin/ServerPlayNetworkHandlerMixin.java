package com.proctoredgames.tiled.mixin;

import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    // Vanilla 1.20.1 rejects creative slot updates whose count exceeds a
    // hardcoded 64, silently deleting any >64 stack (e.g. 96 small tiles)
    // placed from the creative inventory. Accept up to the item's own max
    // count instead, as 1.20.5+ vanilla does.
    @ModifyConstant(method = "onCreativeInventoryAction", constant = @Constant(intValue = 64))
    private int tiled$allowLargeCreativeStacks(int vanillaMax, CreativeInventoryActionC2SPacket packet) {
        return Math.max(vanillaMax, packet.getItemStack().getMaxCount());
    }
}
