package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class TiledModelLoadingPlugin {

    public static final ModelIdentifier SMALL_TILE_BLOCK_MODEL =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "small_tile_block"), "");
    public static final ModelIdentifier SMALL_TILE_BLOCK_MODEL_ITEM =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "small_tile_block"), "inventory");

    public static final ModelIdentifier TILE_BLOCK_MODEL =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "tile_block"), "");
    public static final ModelIdentifier TILE_BLOCK_MODEL_ITEM =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "tile_block"), "inventory");

    public static void register() {
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelId, context) -> {
            if (modelId.equals(SMALL_TILE_BLOCK_MODEL) || modelId.equals(SMALL_TILE_BLOCK_MODEL_ITEM)) {
                return new SmallTileBlockModel();
            } else if (modelId.equals(TILE_BLOCK_MODEL) || modelId.equals(TILE_BLOCK_MODEL_ITEM)) {
                return new TileBlockModel();
            }
            return null; // return null to let other providers handle it
        });
    }
}