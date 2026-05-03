package com.proctoredgames.tiled;

import com.proctoredgames.tiled.block.entity.renderer.TiledModelLoadingPlugin;
import com.proctoredgames.tiled.screen.ModScreenHandlers;
import com.proctoredgames.tiled.screen.custom.TilingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TiledClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TiledModelLoadingPlugin.register();

        HandledScreens.register(ModScreenHandlers.TILING_TABLE_SCREEN_HANDLER, TilingTableScreen::new);
    }
}