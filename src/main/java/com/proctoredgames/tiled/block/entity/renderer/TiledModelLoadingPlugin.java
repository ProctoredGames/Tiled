package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TiledModelLoadingPlugin implements ModelLoadingPlugin {
    private static final Identifier SMALL_TILE_BLOCK_ID = Identifier.of(Tiled.MOD_ID, "small_tile_block");
    private static final Identifier TILE_BLOCK_ID = Identifier.of(Tiled.MOD_ID, "tile_block");
    private static final Identifier SMALL_TILE_LAYER_ID = Identifier.of(Tiled.MOD_ID, "small_tile_layer");
    private static final Identifier TILE_LAYER_ID = Identifier.of(Tiled.MOD_ID, "tile_layer");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            final ModelIdentifier id = context.topLevelId();
            if (id == null) return original;

            Identifier blockId = id.id();
            if (blockId.equals(SMALL_TILE_BLOCK_ID)) {
                return new SmallTileBlockModel();
            } else if (blockId.equals(TILE_BLOCK_ID)) {
                return new TileBlockModel();
            } else if (blockId.equals(SMALL_TILE_LAYER_ID)) {
                return new SmallTileLayerBlockModel();
            } else if (blockId.equals(TILE_LAYER_ID)) {
                return new TileLayerBlockModel();
            }
            return original;
        });
    }
}
