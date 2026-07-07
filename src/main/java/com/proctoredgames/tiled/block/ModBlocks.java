package com.proctoredgames.tiled.block;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.custom.SmallTileBlock;
import com.proctoredgames.tiled.block.custom.SmallTileLayerBlock;
import com.proctoredgames.tiled.block.custom.TileBlock;
import com.proctoredgames.tiled.block.custom.TileLayerBlock;
import com.proctoredgames.tiled.block.custom.TilingTableBlock;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block SMALL_TILE_BLOCK = registerBlock("small_tile_block",
            new SmallTileBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)));

    public static final Block TILE_BLOCK = registerBlock("tile_block",
            new TileBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)));

    public static final Block SMALL_TILE_LAYER = registerBlock("small_tile_layer",
            new SmallTileLayerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).noCollision().pistonBehavior(PistonBehavior.DESTROY)),
            new Item.Settings().maxCount(96));

    public static final Block TILE_LAYER = registerBlock("tile_layer",
            new TileLayerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE).noCollision().pistonBehavior(PistonBehavior.DESTROY)),
            new Item.Settings().maxCount(96));

    public static final Block TILING_TABLE = registerBlock("tiling_table",
            new TilingTableBlock(AbstractBlock.Settings.copy(Blocks.BRICKS)));

    private static Block registerBlock(String name, Block block) {
        return registerBlock(name, block, new Item.Settings());
    }

    private static Block registerBlock(String name, Block block, Item.Settings itemSettings) {
        registerBlockItem(name, block, itemSettings);
        return Registry.register(Registries.BLOCK, Identifier.of(Tiled.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, Item.Settings itemSettings) {
        Registry.register(Registries.ITEM, Identifier.of(Tiled.MOD_ID, name),
                new BlockItem(block, itemSettings));
    }

    public static void registerModBlocks() {
        Tiled.LOGGER.info("Registering Mod Blocks for " + Tiled.MOD_ID);
    }
}

