package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
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

public class CraftingTileBlock extends SpecialCraftingRecipe {

    public CraftingTileBlock(Identifier id, CraftingRecipeCategory category) {
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

        Tiles tiles = new Tiles(
                TileResolver.resolve(input.getStack(topLeft)).getItem(),
                TileResolver.resolve(input.getStack(topLeft + 1)).getItem(),
                TileResolver.resolve(input.getStack(topLeft + w)).getItem(),
                TileResolver.resolve(input.getStack(topLeft + w + 1)).getItem()
        );

        ItemStack stack = TileBlockBE.getStackWith(tiles);
        stack.setCount(4);
        return stack;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    private int findTopLeft(RecipeInputInventory input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int i0 = x + y * width;
                int i1 = i0 + 1;
                int i2 = i0 + width;
                int i3 = i2 + 1;

                if (!isValidIngredient(TileResolver.resolve(input.getStack(i0))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStack(i1))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStack(i2))) ||
                        !isValidIngredient(TileResolver.resolve(input.getStack(i3)))) {
                    continue;
                }

                boolean othersEmpty = true;
                for (int i = 0; i < input.size(); i++) {
                    if (i != i0 && i != i1 && i != i2 && i != i3) {
                        if (!input.getStack(i).isEmpty()) {
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
        return stack.isIn(ModTags.Items.CONCRETE);
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