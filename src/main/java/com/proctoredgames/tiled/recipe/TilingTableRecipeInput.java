package com.proctoredgames.tiled.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;

public record TilingTableRecipeInput(DefaultedList<ItemStack> input) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return input.get(slot);
    }

    /**
     * Returns the effective stack for recipe matching — monochrome tile/small tile
     * blocks are resolved to their corresponding concrete item.
     */
    public ItemStack getResolvedStack(int slot) {
        return TileResolver.resolve(input.get(slot));
    }

    @Override
    public int getSize() {
        return input.size();
    }

    public int getWidth() {
        return 4;
    }

    public int getHeight() {
        return 4;
    }
}