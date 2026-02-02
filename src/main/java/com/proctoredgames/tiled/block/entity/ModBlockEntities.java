package com.proctoredgames.tiled.block.entity;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockEntity;
import com.proctoredgames.tiled.block.entity.custom.TileBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

        public static final BlockEntityType<SmallTileBlockEntity> SMALL_TILE_BLOCK_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Tiled.MOD_ID, "small_tile_block_be"),
                    BlockEntityType.Builder.create(SmallTileBlockEntity::new, ModBlocks.SMALL_TILE_BLOCK).build(null));

    public static final BlockEntityType<TileBlockEntity> TILE_BLOCK_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Tiled.MOD_ID, "tile_block_be"),
                    BlockEntityType.Builder.create(TileBlockEntity::new, ModBlocks.TILE_BLOCK).build(null));

    public static void registerBlockEntities() {
        Tiled.LOGGER.info("Registering Block Entities for " + Tiled.MOD_ID);
    }
}