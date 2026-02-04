package com.proctoredgames.tiled;

import com.proctoredgames.tiled.block.entity.renderer.TiledModelLoadingPlugin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class TiledClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new TiledModelLoadingPlugin());
    }
}
