package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class CraftingTileBlockRecipe extends SpecialCraftingRecipe {

    // Stores where the valid 2x2 square starts
    private int topLeftSlot = -1;

    public CraftingTileBlockRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {

        if (!this.fits(input.getWidth(), input.getHeight())) {
            return false;
        }

        int width = input.getWidth();
        int height = input.getHeight();

        // Try every possible 2x2 position
        for (int y = 0; y <= height - 2; y++) {
            for (int x = 0; x <= width - 2; x++) {

                int i0 = x + y * width;
                int i1 = (x + 1) + y * width;
                int i2 = x + (y + 1) * width;
                int i3 = (x + 1) + (y + 1) * width;

                if (isValidIngredient(input.getStackInSlot(i0)) &&
                        isValidIngredient(input.getStackInSlot(i1)) &&
                        isValidIngredient(input.getStackInSlot(i2)) &&
                        isValidIngredient(input.getStackInSlot(i3))) {

                    // Ensure everything else is empty
                    for (int i = 0; i < input.getSize(); i++) {
                        if (i != i0 && i != i1 && i != i2 && i != i3) {
                            if (!input.getStackInSlot(i).isEmpty()) {
                                return false;
                            }
                        }
                    }

                    topLeftSlot = i0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValidIngredient(ItemStack stack) {
        return stack.isIn(ItemTags.TERRACOTTA);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {

        if (topLeftSlot == -1) {
            return ItemStack.EMPTY;
        }

        int w = input.getWidth();
        int i0 = topLeftSlot;
        int i1 = topLeftSlot + 1;
        int i2 = topLeftSlot + w;
        int i3 = topLeftSlot + w + 1;

        Tiles tiles = new Tiles(
                input.getStackInSlot(i0).getItem(),
                input.getStackInSlot(i1).getItem(),
                input.getStackInSlot(i2).getItem(),
                input.getStackInSlot(i3).getItem()
        );

        return TileBlockBE.getStackWith(tiles);
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_TILE_BLOCK;
    }
}

