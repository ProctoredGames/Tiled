package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.Tiled;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeType<TilingTableRecipe> TILING_TABLE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Tiled.MOD_ID, "tiling_table"), new RecipeType<TilingTableRecipe>() {
            @Override
            public String toString() {
                return "tiling_table";
            }
        });
    public static void registerRecipes() {
        Tiled.LOGGER.info("Registering Custom Recipes for " + Tiled.MOD_ID);
    }
}
