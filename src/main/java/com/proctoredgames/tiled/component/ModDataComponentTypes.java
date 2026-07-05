package com.proctoredgames.tiled.component;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.SmallTilesPerFace;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.block.entity.records.TilesPerFace;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<SmallTiles> SMALL_TILE_BLOCK_TILES =
            register("small_tile_block_tiles", builder -> builder.codec(SmallTiles.CODEC)
    );

    public static final ComponentType<Tiles> TILE_BLOCK_TILES =
            register("tile_block_tiles", builder -> builder.codec(Tiles.CODEC)
    );

    // Used instead of the uniform components when a block's faces have been
    // edited individually with the tile trowel
    public static final ComponentType<SmallTilesPerFace> SMALL_TILE_BLOCK_FACE_TILES =
            register("small_tile_block_face_tiles", builder -> builder.codec(SmallTilesPerFace.CODEC)
    );

    public static final ComponentType<TilesPerFace> TILE_BLOCK_FACE_TILES =
            register("tile_block_face_tiles", builder -> builder.codec(TilesPerFace.CODEC)
    );

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator){
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Tiled.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes(){
        Tiled.LOGGER.info("Registering Data Component for " + Tiled.MOD_ID);
    }


}
