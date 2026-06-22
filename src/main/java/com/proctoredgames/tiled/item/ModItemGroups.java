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
                        entries.add(ModItems.SMALL_WHITE_TILES);
                        entries.add(ModItems.SMALL_ORANGE_TILES);
                        entries.add(ModItems.SMALL_MAGENTA_TILES);
                        entries.add(ModItems.SMALL_LIGHT_BLUE_TILES);
                        entries.add(ModItems.SMALL_YELLOW_TILES);
                        entries.add(ModItems.SMALL_LIME_TILES);
                        entries.add(ModItems.SMALL_PINK_TILES);
                        entries.add(ModItems.SMALL_GRAY_TILES);
                        entries.add(ModItems.SMALL_LIGHT_GRAY_TILES);
                        entries.add(ModItems.SMALL_CYAN_TILES);
                        entries.add(ModItems.SMALL_PURPLE_TILES);
                        entries.add(ModItems.SMALL_BLUE_TILES);
                        entries.add(ModItems.SMALL_BROWN_TILES);
                        entries.add(ModItems.SMALL_GREEN_TILES);
                        entries.add(ModItems.SMALL_RED_TILES);
                        entries.add(ModItems.SMALL_BLACK_TILES);
                        entries.add(ModItems.WHITE_TILES);
                        entries.add(ModItems.ORANGE_TILES);
                        entries.add(ModItems.MAGENTA_TILES);
                        entries.add(ModItems.LIGHT_BLUE_TILES);
                        entries.add(ModItems.YELLOW_TILES);
                        entries.add(ModItems.LIME_TILES);
                        entries.add(ModItems.PINK_TILES);
                        entries.add(ModItems.GRAY_TILES);
                        entries.add(ModItems.LIGHT_GRAY_TILES);
                        entries.add(ModItems.CYAN_TILES);
                        entries.add(ModItems.PURPLE_TILES);
                        entries.add(ModItems.BLUE_TILES);
                        entries.add(ModItems.BROWN_TILES);
                        entries.add(ModItems.GREEN_TILES);
                        entries.add(ModItems.RED_TILES);
                        entries.add(ModItems.BLACK_TILES);
                        entries.add(ModItems.TILE_TROWEL);
                        entries.add(ModBlocks.TILING_TABLE);
                    }).build());

    public static void registerItemGroups() {
        Tiled.LOGGER.info("Registering Item Groups for " + Tiled.MOD_ID);
    }
}
