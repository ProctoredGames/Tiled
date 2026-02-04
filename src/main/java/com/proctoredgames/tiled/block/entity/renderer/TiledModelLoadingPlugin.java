package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TiledModelLoadingPlugin implements ModelLoadingPlugin {
    public static final ModelIdentifier TILE_BLOCK_MODEL = new ModelIdentifier(Identifier.of(Tiled.MOD_ID, "tile_block"), "");
    public static final ModelIdentifier TILE_BLOCK_MODEL_ITEM = new ModelIdentifier(Identifier.of(Tiled.MOD_ID, "tile_block"), "inventory");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        // We want to add our model when the models are loaded
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            // This is called for every model that is loaded, so make sure we only target ours
            final ModelIdentifier id = context.topLevelId();
            if (id != null && (id.equals(TILE_BLOCK_MODEL) || id.equals(TILE_BLOCK_MODEL_ITEM))) {
                return new TileBlockModel();
            } else {
                // If we don't modify the model we just return the original as-is
                return original;
            }
        });
    }
}
