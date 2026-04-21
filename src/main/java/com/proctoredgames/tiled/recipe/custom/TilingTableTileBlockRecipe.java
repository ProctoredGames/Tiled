package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.recipe.TileResolver;
import com.proctoredgames.tiled.recipe.TilingTableRecipeInput;
import com.proctoredgames.tiled.util.ModTags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record TilingTableTileBlockRecipe(Ingredient inputItem, ItemStack output) implements Recipe<TilingTableRecipeInput> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(TilingTableRecipeInput input, World world) {
        return findTopLeft(input) != -1;
    }

    @Override
    public ItemStack craft(TilingTableRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return ItemStack.EMPTY;

        int w = input.getWidth();

        Tiles tiles = new Tiles(
                TileResolver.resolve(input.getStackInSlot(topLeft)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + 1)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + w)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + w + 1)).getItem()
        );

        ItemStack result = TileBlockBE.getStackWith(tiles);
        result.setCount(4);
        return result;
    }

    private int findTopLeft(TilingTableRecipeInput input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int i0 = x + y * width;
                int i1 = i0 + 1;
                int i2 = i0 + width;
                int i3 = i2 + 1;

                if (!isValidIngredient(TileResolver.resolve(input.getStackInSlot(i0))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStackInSlot(i1))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStackInSlot(i2))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStackInSlot(i3)))) {
                    continue;
                }

                // All other slots must be empty
                boolean othersEmpty = true;
                for (int i = 0; i < input.getSize(); i++) {
                    if (i != i0 && i != i1 && i != i2 && i != i3) {
                        if (!input.getStackInSlot(i).isEmpty()) {
                            othersEmpty = false;
                            break;
                        }
                    }
                }

                if (othersEmpty) return i0;
            }
        }

        return -1;
    }

    private boolean isValidIngredient(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ModTags.Items.CONCRETE);
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TILING_TABLE_TILE_BLOCK_RECIPE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TILING_TABLE_TILE_BLOCK_TYPE;
    }
}