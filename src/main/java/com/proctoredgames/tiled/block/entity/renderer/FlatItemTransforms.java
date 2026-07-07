package com.proctoredgames.tiled.block.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import org.joml.Vector3f;

// The display transforms of minecraft:item/generated, so the layer items
// render as flat 2d sprites like glow lichen
@Environment(EnvType.CLIENT)
public class FlatItemTransforms {

    public static final ModelTransformation FLAT_ITEM = new ModelTransformation(
            thirdPerson(),
            thirdPerson(),
            firstPerson(),
            firstPerson(),
            new Transformation(new Vector3f(0, 180, 0), new Vector3f(0, 13f / 16f, 7f / 16f), new Vector3f(1, 1, 1)),
            Transformation.IDENTITY,
            new Transformation(new Vector3f(), new Vector3f(0, 2f / 16f, 0), new Vector3f(0.5f, 0.5f, 0.5f)),
            new Transformation(new Vector3f(0, 180, 0), new Vector3f(), new Vector3f(1, 1, 1))
    );

    private static Transformation thirdPerson() {
        return new Transformation(new Vector3f(), new Vector3f(0, 3f / 16f, 1f / 16f), new Vector3f(0.55f, 0.55f, 0.55f));
    }

    private static Transformation firstPerson() {
        return new Transformation(new Vector3f(0, -90, 25), new Vector3f(1.13f / 16f, 3.2f / 16f, 1.13f / 16f), new Vector3f(0.68f, 0.68f, 0.68f));
    }
}
