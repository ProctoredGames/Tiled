package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.TileResolver;
import com.proctoredgames.tiled.util.ModTags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CraftingTileBlock extends SpecialCraftingRecipe {

    public CraftingTileBlock(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return findTopLeft(input) != -1;
    }

    private boolean isValidIngredient(ItemStack stack) {
        return stack.isIn(ModTags.Items.CONCRETE);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) {
            return ItemStack.EMPTY;
        }

        int w = input.getWidth();

        Tiles tiles = new Tiles(
                TileResolver.resolve(input.getStackInSlot(topLeft)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + 1)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + w)).getItem(),
                TileResolver.resolve(input.getStackInSlot(topLeft + w + 1)).getItem()
        );

        ItemStack stack = TileBlockBE.getStackWith(tiles);
        stack.setCount(4);
        return stack;
    }

    private int findTopLeft(CraftingRecipeInput input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {

                int i0 = x + y * width;
                int i1 = i0 + 1;
                int i2 = i0 + width;
                int i3 = i2 + 1;

                if (isValidIngredient(input.getStackInSlot(i0)) &&
                        isValidIngredient(input.getStackInSlot(i1)) &&
                        isValidIngredient(input.getStackInSlot(i2)) &&
                        isValidIngredient(input.getStackInSlot(i3))) {

                    // ensure others empty
                    for (int i = 0; i < input.getSize(); i++) {
                        if (i != i0 && i != i1 && i != i2 && i != i3) {
                            if (!input.getStackInSlot(i).isEmpty()) {
                                return -1;
                            }
                        }
                    }

                    return i0;
                }
            }
        }

        return -1;
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