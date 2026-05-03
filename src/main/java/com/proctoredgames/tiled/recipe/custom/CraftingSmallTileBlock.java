package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.TileResolver;
import com.proctoredgames.tiled.util.ModTags;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// 1.20.1: SpecialCraftingRecipe is Recipe<RecipeInputInventory>, so matches/craft take RecipeInputInventory.
// The constructor is (Identifier, CraftingRecipeCategory) to match SpecialRecipeSerializer.Factory.
public class CraftingSmallTileBlock extends SpecialCraftingRecipe {

    public CraftingSmallTileBlock(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory input, World world) {
        return findTopLeft(input) != -1;
    }

    @Override
    public ItemStack craft(RecipeInputInventory input, DynamicRegistryManager registryManager) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return ItemStack.EMPTY;

        int w = input.getWidth();
        ItemStack[] stacks = new ItemStack[16];
        int index = 0;

        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int slot = (topLeft % w + dx) + ((topLeft / w) + dy) * w;
                stacks[index++] = TileResolver.resolve(input.getStack(slot));
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

        ItemStack stack = SmallTileBlockBE.getStackWith(tiles);
        stack.setCount(16);
        return stack;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    private int findTopLeft(RecipeInputInventory input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y <= height - 4; y++) {
            for (int x = 0; x <= width - 4; x++) {
                boolean valid = true;
                for (int dy = 0; dy < 4 && valid; dy++) {
                    for (int dx = 0; dx < 4 && valid; dx++) {
                        int slot = (x + dx) + (y + dy) * width;
                        if (!isValidIngredient(TileResolver.resolve(input.getStack(slot)))) {
                            valid = false;
                        }
                    }
                }
                if (valid) return x + y * width;
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