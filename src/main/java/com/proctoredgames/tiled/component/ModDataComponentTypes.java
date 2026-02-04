package com.proctoredgames.tiled.component;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.Tiles;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<Tiles> TILE_BLOCK_TILES = register(
            "tile_block_tiles", builder -> builder.codec(Tiles.CODEC).packetCodec(Tiles.PACKET_CODEC).cache()
    );

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator){
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Tiled.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes(){
        Tiled.LOGGER.info("Registering Data Component for " + Tiled.MOD_ID);
    }


}
