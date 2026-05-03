package com.proctoredgames.tiled.block.entity;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;

import com.proctoredgames.tiled.block.entity.custom.TilingTableBE;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

        public static final BlockEntityType<SmallTileBlockBE> SMALL_TILE_BLOCK_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Tiled.MOD_ID,"small_tile_block_be"),
                    BlockEntityType.Builder.create(SmallTileBlockBE::new, ModBlocks.SMALL_TILE_BLOCK).build(null));

    public static final BlockEntityType<TileBlockBE> TILE_BLOCK_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Tiled.MOD_ID,"tile_block_be"),
                    BlockEntityType.Builder.create(TileBlockBE::new, ModBlocks.TILE_BLOCK).build(null));

    public static final BlockEntityType<TilingTableBE> TILING_TABLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Tiled.MOD_ID,"tiling_table_be"),
                    BlockEntityType.Builder.create(TilingTableBE::new, ModBlocks.TILING_TABLE).build(null));

    public static void registerBlockEntities() {
        Tiled.LOGGER.info("Registering Block Entities for " + Tiled.MOD_ID);
    }
}