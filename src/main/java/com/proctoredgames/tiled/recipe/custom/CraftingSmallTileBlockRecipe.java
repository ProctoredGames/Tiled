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

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) {
            return ItemStack.EMPTY;
        }

        int w = input.getWidth();
        ItemStack[] stacks = new ItemStack[16];
        int index = 0;

        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int slot = (topLeft % w + dx) + ((topLeft / w) + dy) * w;
                stacks[index++] = input.getStackInSlot(slot);
            }
        }

        SmallTiles tiles = new SmallTiles(
                stacks[0].getItem(),  stacks[1].getItem(),
                stacks[2].getItem(),  stacks[3].getItem(),
                stacks[4].getItem(),  stacks[5].getItem(),
                stacks[6].getItem(),  stacks[7].getItem(),
                stacks[8].getItem(),  stacks[9].getItem(),
                stacks[10].getItem(), stacks[11].getItem(),
                stacks[12].getItem(), stacks[13].getItem(),
                stacks[14].getItem(), stacks[15].getItem()
        );

        return SmallTileBlockBE.getStackWith(tiles);
    }

    private int findTopLeft(CraftingRecipeInput input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y <= height - 4; y++) {
            for (int x = 0; x <= width - 4; x++) {
                boolean valid = true;

                for (int dy = 0; dy < 4 && valid; dy++) {
                    for (int dx = 0; dx < 4 && valid; dx++) {
                        int slot = (x + dx) + (y + dy) * width;
                        if (!isValidIngredient(input.getStackInSlot(slot))) {
                            valid = false;
                        }
                    }
                }

                if (valid) {
                    int topLeft = x + y * width;

                    // Ensure all slots outside the 4x4 block are empty
                    for (int i = 0; i < input.getSize(); i++) {
                        int ix = i % width;
                        int iy = i / width;
                        boolean inBlock = ix >= x && ix < x + 4 && iy >= y && iy < y + 4;
                        if (!inBlock && !input.getStackInSlot(i).isEmpty()) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) return topLeft;
                }
            }
        }

        return -1;
    }

    private boolean isValidIngredient(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ModTags.Items.CONCRETE);
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