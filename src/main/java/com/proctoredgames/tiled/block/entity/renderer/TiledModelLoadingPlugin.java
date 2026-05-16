package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TiledModelLoadingPlugin {

    private static final Identifier SMALL_TILE_BLOCK_ID = new Identifier(Tiled.MOD_ID, "small_tile_block");
    private static final Identifier TILE_BLOCK_ID = new Identifier(Tiled.MOD_ID, "tile_block");

    public static void register() {
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelId, context) -> {
            String path = modelId.getPath();
            String namespace = modelId.getNamespace();
            if (namespace.equals(Tiled.MOD_ID) && path.equals("small_tile_block")) {
                return new SmallTileBlockModel();
            } else if (namespace.equals(Tiled.MOD_ID) && path.equals("tile_block")) {
                return new TileBlockModel();
            }
            return null;
        });
    }
}