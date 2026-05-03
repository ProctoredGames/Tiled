package com.proctoredgames.tiled.screen;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.screen.custom.TilingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<TilingTableScreenHandler> TILING_TABLE_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    new Identifier(Tiled.MOD_ID, "tiling_table_screen_handler"),
                    new ExtendedScreenHandlerType<>(TilingTableScreenHandler::new)
            );

    public static void registerScreenHandlers() {
        Tiled.LOGGER.info("registering Screen Handlers for " + Tiled.MOD_ID);
    }
}