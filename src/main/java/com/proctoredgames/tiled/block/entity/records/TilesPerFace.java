package com.proctoredgames.tiled.block.entity.records;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.Direction;

import java.util.Map;

public record TilesPerFace(Map<Direction, Tiles> faces) {

    public static final Codec<TilesPerFace> CODEC = Codec.unboundedMap(Direction.CODEC, Tiles.CODEC)
            .xmap(faces -> new TilesPerFace(Map.copyOf(faces)), TilesPerFace::faces);

    public Tiles get(Direction direction) {
        return this.faces.getOrDefault(direction, Tiles.DEFAULT);
    }
}
