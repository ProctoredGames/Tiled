package com.proctoredgames.tiled.block;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.custom.SmallTileBlock;
import com.proctoredgames.tiled.block.custom.TileBlock;
import com.proctoredgames.tiled.block.custom.TilingTableBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block SMALL_TILE_BLOCK = registerBlock("small_tile_block",
            new SmallTileBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK)));

    public static final Block TILE_BLOCK = registerBlock("tile_block",
            new TileBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK)));

    public static final Block TILING_TABLE = registerBlock("tiling_table",
            new TilingTableBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Tiled.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Tiled.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Tiled.LOGGER.info("Registering Mod Blocks for " + Tiled.MOD_ID);
    }
}

