package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.Tiled;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeType<TilingTableSmallTileBlockRecipe> TILING_TABLE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Tiled.MOD_ID, "tiling_table"), new RecipeType<>() {
                @Override
                public String toString() { return "tiling_table"; }
            });

    public static final RecipeType<TilingTableTileBlockRecipe> TILING_TABLE_TILE_BLOCK_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Tiled.MOD_ID, "tiling_table_tile_block"), new RecipeType<>() {
                @Override
                public String toString() { return "tiling_table_tile_block"; }
            });

    public static void registerRecipes() {
        Tiled.LOGGER.info("Registering Custom Recipes for " + Tiled.MOD_ID);
    }
}