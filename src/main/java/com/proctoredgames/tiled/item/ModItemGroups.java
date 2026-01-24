package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup TILED_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Tiled.MOD_ID, "tiled"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.TILE_BLOCK))
                    .displayName(Text.translatable("itemgroup.tiled.tiled"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.TILE_BLOCK);
                    }).build());

    public static void registerItemGroups() {
        Tiled.LOGGER.info("Registering Item Groups for " + Tiled.MOD_ID);
    }
}
