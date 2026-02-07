package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.util.ModTags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CraftingSmallTileBlockRecipe extends SpecialCraftingRecipe {

    public CraftingSmallTileBlockRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return findTopLeft(input) != -1;
    }

    private boolean isValidIngredient(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
//
//        int topLeft = findTopLeft(input);
//        if (topLeft == -1) {
//            return ItemStack.EMPTY;
//        }
//
//        int w = input.getWidth();
//
//        SmallTiles tiles = new SmallTiles(
//                input.getStackInSlot(topLeft).getItem(),
//                input.getStackInSlot(topLeft + 1).getItem(),
//                input.getStackInSlot(topLeft + 2).getItem(),
//                input.getStackInSlot(topLeft + 3).getItem(),
//                input.getStackInSlot(topLeft + w).getItem(),
//                input.getStackInSlot(topLeft + w + 1).getItem(),
//                input.getStackInSlot(topLeft + w + 2).getItem(),
//                input.getStackInSlot(topLeft + w + 3).getItem(),
//                input.getStackInSlot(topLeft + 2*w).getItem(),
//                input.getStackInSlot(topLeft + 2*w + 1).getItem(),
//                input.getStackInSlot(topLeft + 2*w + 2).getItem(),
//                input.getStackInSlot(topLeft + 2*w + 3).getItem(),
//                input.getStackInSlot(topLeft + 3*w).getItem(),
//                input.getStackInSlot(topLeft + 3*w + 1).getItem(),
//                input.getStackInSlot(topLeft + 3*w + 2).getItem(),
//                input.getStackInSlot(topLeft + 3*w + 3).getItem()
//        );
//
//        return SmallTileBlockBE.getStackWith(tiles);
        return null;
    }

    private int findTopLeft(CraftingRecipeInput input) {

        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {

                int i0 = x + y * width;
                int i1 = i0 + 1;
                int i2 = i0 + 2;
                int i3 = i2 + 3;
                int i4 = x + y * width;
                int i5 = i0 + 1;
                int i6 = i0 + width;
                int i7 = i2 + 1;
                int i8 = x + y * width;
                int i9 = i0 + 1;
                int i10 = i0 + width;
                int i11 = i2 + 1;
                int i12 = x + y * width;
                int i13 = i0 + 1;
                int i14 = i0 + width;
                int i15 = i2 + 1;

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
        return width >= 4 && height >= 4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_SMALL_TILE_BLOCK;
    }
}

