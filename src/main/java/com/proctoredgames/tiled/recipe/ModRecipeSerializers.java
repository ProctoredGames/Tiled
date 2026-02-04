package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.recipe.custom.CraftingTileBlockRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {

    public static final RecipeSerializer<CraftingTileBlockRecipe> CRAFTING_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingTileBlockRecipe::new);

    public static void register() {
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "crafting_tile_block"),
                CRAFTING_TILE_BLOCK
        );
        Tiled.LOGGER.info("Registering recipe serializers for " + Tiled.MOD_ID);
    }
}
