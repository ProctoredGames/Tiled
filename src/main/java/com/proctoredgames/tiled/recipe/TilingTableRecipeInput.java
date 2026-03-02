package com.proctoredgames.tiled.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;

public record TilingTableRecipeInput(DefaultedList<ItemStack> input) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return input.get(slot);
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