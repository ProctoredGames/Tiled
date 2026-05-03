package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TiledModelLoadingPlugin implements ModelLoadingPlugin {

    // 1.20.1: ModelIdentifier still takes (Identifier, variant) but Identifier.of() -> new Identifier()
    public static final ModelIdentifier SMALL_TILE_BLOCK_MODEL =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "small_tile_block"), "");
    public static final ModelIdentifier SMALL_TILE_BLOCK_MODEL_ITEM =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "small_tile_block"), "inventory");

    public static final ModelIdentifier TILE_BLOCK_MODEL =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "tile_block"), "");
    public static final ModelIdentifier TILE_BLOCK_MODEL_ITEM =
            new ModelIdentifier(new Identifier(Tiled.MOD_ID, "tile_block"), "inventory");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            final ModelIdentifier id = context.topLevelId();
            if (id != null && (id.equals(SMALL_TILE_BLOCK_MODEL) || id.equals(SMALL_TILE_BLOCK_MODEL_ITEM))) {
                return new SmallTileBlockModel();
            } else if (id != null && (id.equals(TILE_BLOCK_MODEL) || id.equals(TILE_BLOCK_MODEL_ITEM))) {
                return new TileBlockModel();
            } else {
                return original;
            }
        });
    }
}