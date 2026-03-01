package com.proctoredgames.tiled.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;

public abstract class SpecialTilingTableRecipe implements TilingTableRecipe {
    private final CraftingRecipeCategory category;

    public SpecialTilingTableRecipe(CraftingRecipeCategory category) {
        this.category = category;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return this.category;
    }
}

