package com.proctoredgames.tiled.screen;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.screen.custom.TilingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<TilingTableScreenHandler> TILING_TABLE_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of(Tiled.MOD_ID, "tiling_table_screen_handler"),
                    new ScreenHandlerType<>(TilingTableScreenHandler::new, FeatureSet.empty())
            );

    public static void registerScreenHandlers() {
        Tiled.LOGGER.info("registering Screen Handlers for " + Tiled.MOD_ID);
    }
}
