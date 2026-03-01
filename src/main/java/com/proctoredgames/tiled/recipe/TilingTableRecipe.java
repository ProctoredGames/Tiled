package com.proctoredgames.tiled.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;

public interface TilingTableRecipe extends Recipe<TilingTableRecipeInput> {
    @Override
    default RecipeType<?> getType() {
        return ModRecipes.TILING_TABLE_TYPE;
    }

    CraftingRecipeCategory getCategory();
}

