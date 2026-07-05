package com.proctoredgames.tiled.block;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.custom.SmallTileBlock;
import com.proctoredgames.tiled.block.custom.SmallTilesBlock;
import com.proctoredgames.tiled.block.custom.TileBlock;
import com.proctoredgames.tiled.block.custom.TilesBlock;
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

    public static final Block SMALL_TILES = registerBlock("small_tiles",
            new SmallTilesBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK).noCollision().nonOpaque()),
            new Item.Settings().maxCount(96));

    public static final Block TILES = registerBlock("tiles",
            new TilesBlock(AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK).noCollision().nonOpaque()),
            new Item.Settings().maxCount(96));

    public static final Block TILING_TABLE = registerBlock("tiling_table",
            new TilingTableBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_ANDESITE)));

    private static Block registerBlock(String name, Block block) {
        return registerBlock(name, block, new Item.Settings());
    }

    private static Block registerBlock(String name, Block block, Item.Settings itemSettings) {
        registerBlockItem(name, block, itemSettings);
        return Registry.register(Registries.BLOCK, new Identifier(Tiled.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, Item.Settings itemSettings) {
        Registry.register(Registries.ITEM, new Identifier(Tiled.MOD_ID, name),
                new BlockItem(block, itemSettings));
    }

    public static void registerModBlocks() {
        Tiled.LOGGER.info("Registering Mod Blocks for " + Tiled.MOD_ID);
    }
}

