package com.proctoredgames.tiled.block.entity.records;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.Direction;

import java.util.Map;

public record SmallTilesPerFace(Map<Direction, SmallTiles> faces) {

    public static final Codec<SmallTilesPerFace> CODEC = Codec.unboundedMap(Direction.CODEC, SmallTiles.CODEC)
            .xmap(faces -> new SmallTilesPerFace(Map.copyOf(faces)), SmallTilesPerFace::faces);

    public SmallTiles get(Direction direction) {
        return this.faces.getOrDefault(direction, SmallTiles.DEFAULT);
    }
}
