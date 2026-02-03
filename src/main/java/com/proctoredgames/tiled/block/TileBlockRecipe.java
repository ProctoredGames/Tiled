package com.proctoredgames.tiled.block;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

public class TileBlockRecipe extends ShapedRecipe {
    private static final Item[] TERRACOTTA_ITEMS = {
            Items.BLACK_TERRACOTTA, Items.BLUE_TERRACOTTA, Items.BROWN_TERRACOTTA, Items.CYAN_TERRACOTTA,
            Items.GRAY_TERRACOTTA, Items.GREEN_TERRACOTTA, Items.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_GRAY_TERRACOTTA,
            Items.LIME_TERRACOTTA, Items.MAGENTA_TERRACOTTA, Items.ORANGE_TERRACOTTA, Items.PINK_TERRACOTTA,
            Items.PURPLE_TERRACOTTA, Items.RED_TERRACOTTA, Items.WHITE_TERRACOTTA, Items.YELLOW_TERRACOTTA,
    };

    public TileBlockRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
    }

    public TileBlockRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result) {
        super(group, category, raw, result);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack result = super.craft()
    }
}
