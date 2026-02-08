package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> TILED_GROUP_KEY =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(Tiled.MOD_ID, "tiled"));

    public static final ItemGroup TILED_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Tiled.MOD_ID, "tiled"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.SMALL_TILE_BLOCK))
                    .displayName(Text.translatable("itemgroup.tiled.tiled"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.TILING_TABLE);
                    }).build());

    public static void registerItemGroups() {
        Tiled.LOGGER.info("Registering Item Groups for " + Tiled.MOD_ID);
    }
}
