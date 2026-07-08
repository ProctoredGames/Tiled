package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> TILED_GROUP_KEY =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(Tiled.MOD_ID, "tiled"));

    public static final Tiles icon_tiles = new Tiles(Items.BLUE_CONCRETE, Items.YELLOW_CONCRETE, Items.ORANGE_CONCRETE, Items.PURPLE_CONCRETE);

    public static final ItemGroup TILED_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Tiled.MOD_ID, "tiled"),
            FabricItemGroup.builder().icon(() -> TileBlockBE.getStackWith(icon_tiles))
                    .displayName(Text.translatable("itemgroup.tiled.tiled"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.SMALL_WHITE_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_LIGHT_GRAY_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_GRAY_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_BLACK_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_BROWN_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_RED_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_ORANGE_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_YELLOW_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_LIME_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_GREEN_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_CYAN_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_LIGHT_BLUE_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_BLUE_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_PURPLE_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_MAGENTA_CONCRETE_TILE);
                        entries.add(ModItems.SMALL_PINK_CONCRETE_TILE);
                        entries.add(ModItems.WHITE_CONCRETE_TILE);
                        entries.add(ModItems.LIGHT_GRAY_CONCRETE_TILE);
                        entries.add(ModItems.GRAY_CONCRETE_TILE);
                        entries.add(ModItems.BLACK_CONCRETE_TILE);
                        entries.add(ModItems.BROWN_CONCRETE_TILE);
                        entries.add(ModItems.RED_CONCRETE_TILE);
                        entries.add(ModItems.ORANGE_CONCRETE_TILE);
                        entries.add(ModItems.YELLOW_CONCRETE_TILE);
                        entries.add(ModItems.LIME_CONCRETE_TILE);
                        entries.add(ModItems.GREEN_CONCRETE_TILE);
                        entries.add(ModItems.CYAN_CONCRETE_TILE);
                        entries.add(ModItems.LIGHT_BLUE_CONCRETE_TILE);
                        entries.add(ModItems.BLUE_CONCRETE_TILE);
                        entries.add(ModItems.PURPLE_CONCRETE_TILE);
                        entries.add(ModItems.MAGENTA_CONCRETE_TILE);
                        entries.add(ModItems.PINK_CONCRETE_TILE);
                        entries.add(ModItems.TILE_TROWEL);
                        entries.add(ModBlocks.TILING_TABLE);
                    }).build());

    public static void registerItemGroups() {
        Tiled.LOGGER.info("Registering Item Groups for " + Tiled.MOD_ID);
    }
}