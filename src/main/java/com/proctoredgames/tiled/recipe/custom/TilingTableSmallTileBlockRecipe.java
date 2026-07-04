package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.recipe.TileResolver;
import com.proctoredgames.tiled.recipe.TilingTableRecipeInput;
import com.proctoredgames.tiled.util.ModTags;
import com.proctoredgames.tiled.util.TileColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public record TilingTableSmallTileBlockRecipe(Ingredient inputItem, ItemStack output) implements Recipe<TilingTableRecipeInput> {

    // A small tile block carries 96 small tiles (16 per face x 6 faces), so
    // each of the sixteen slots supplies 6 small tiles per block; one
    // concrete equals 96 small tiles
    public static final int ITEMS_PER_SLOT_PER_BLOCK = 6;
    public static final int MAX_BLOCKS = 16;
    public static final int MAX_LAYERS = 96;

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
        Item[] items = new Item[16];
        int index = 0;

        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int slot = (topLeft % w + dx) + ((topLeft / w) + dy) * w;
                items[index++] = slotConcrete(input.getStackInSlot(slot));
            }
        }

        SmallTiles tiles = new SmallTiles(
                items[0], items[1], items[2], items[3],
                items[4], items[5], items[6], items[7],
                items[8], items[9], items[10], items[11],
                items[12], items[13], items[14], items[15]
        );

        ItemStack stack = SmallTileBlockBE.getStackWith(tiles);
        stack.setCount(MAX_BLOCKS);
        return stack;
    }

    // Output scales with the slot that can cover the fewest blocks/layers:
    // a concrete covers a full batch, small tile items cover what their count allows
    public int computeOutputCount(TilingTableRecipeInput input, boolean layerMode) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return 0;

        int count = layerMode ? MAX_LAYERS : MAX_BLOCKS;
        int w = input.getWidth();
        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int slot = (topLeft % w + dx) + ((topLeft / w) + dy) * w;
                ItemStack stack = input.getStackInSlot(slot);
                if (isConcrete(stack)) continue;
                int worth = layerMode ? stack.getCount() : stack.getCount() / ITEMS_PER_SLOT_PER_BLOCK;
                count = Math.min(count, worth);
            }
        }
        return count;
    }

    public int[] computeConsumption(TilingTableRecipeInput input, boolean layerMode) {
        int[] amounts = new int[input.getSize()];
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return amounts;

        int outputCount = computeOutputCount(input, layerMode);
        if (outputCount <= 0) return amounts;

        int w = input.getWidth();
        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int slot = (topLeft % w + dx) + ((topLeft / w) + dy) * w;
                amounts[slot] = isConcrete(input.getStackInSlot(slot)) ? 1
                        : (layerMode ? outputCount : outputCount * ITEMS_PER_SLOT_PER_BLOCK);
            }
        }
        return amounts;
    }

    private static boolean isConcrete(ItemStack stack) {
        return TileResolver.resolve(stack).isIn(ModTags.Items.CONCRETE);
    }

    @Nullable
    private static Item slotConcrete(ItemStack stack) {
        ItemStack resolved = TileResolver.resolve(stack);
        if (resolved.isIn(ModTags.Items.CONCRETE)) return resolved.getItem();
        return TileColors.concreteForSmallTile(stack.getItem());
    }

    private boolean isValidIngredient(ItemStack stack) {
        return !stack.isEmpty() && slotConcrete(stack) != null;
    }

    private int findTopLeft(TilingTableRecipeInput input) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = 0; y <= height - 4; y++) {
            for (int x = 0; x <= width - 4; x++) {
                boolean valid = true;
                // No mixing: the region must be all concrete or all small tile items
                boolean concrete = isConcrete(input.getStackInSlot(x + y * width));

                for (int dy = 0; dy < 4 && valid; dy++) {
                    for (int dx = 0; dx < 4 && valid; dx++) {
                        int slot = (x + dx) + (y + dy) * width;
                        ItemStack stack = input.getStackInSlot(slot);
                        if (!isValidIngredient(stack) || isConcrete(stack) != concrete) {
                            valid = false;
                        }
                    }
                }

                if (valid) return x + y * width;
            }
        }

        return -1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 4 && height >= 4;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TILING_TABLE_SMALL_TILE_BLOCK_RECIPE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TILING_TABLE_TYPE;
    }
}
