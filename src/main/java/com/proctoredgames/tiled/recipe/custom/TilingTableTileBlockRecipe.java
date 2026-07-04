package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
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

public record TilingTableTileBlockRecipe(Ingredient inputItem, ItemStack output) implements Recipe<TilingTableRecipeInput> {

    // A tile block carries 24 tiles (4 per face x 6 faces), so each of the
    // four slots supplies 6 tiles per block; one concrete equals 24 tiles
    public static final int ITEMS_PER_SLOT_PER_BLOCK = 6;
    public static final int MAX_BLOCKS = 4;
    public static final int MAX_LAYERS = 24;

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
                slotConcrete(input.getStackInSlot(topLeft)),
                slotConcrete(input.getStackInSlot(topLeft + 1)),
                slotConcrete(input.getStackInSlot(topLeft + w)),
                slotConcrete(input.getStackInSlot(topLeft + w + 1))
        );

        ItemStack result = TileBlockBE.getStackWith(tiles);
        result.setCount(MAX_BLOCKS);
        return result;
    }

    // Output scales with the slot that can cover the fewest blocks/layers:
    // a concrete covers a full batch, tile items cover what their count allows
    public int computeOutputCount(TilingTableRecipeInput input, boolean layerMode) {
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return 0;

        int count = layerMode ? MAX_LAYERS : MAX_BLOCKS;
        for (int slot : regionSlots(topLeft, input.getWidth())) {
            ItemStack stack = input.getStackInSlot(slot);
            if (isConcrete(stack)) continue;
            int worth = layerMode ? stack.getCount() : stack.getCount() / ITEMS_PER_SLOT_PER_BLOCK;
            count = Math.min(count, worth);
        }
        return count;
    }

    public int[] computeConsumption(TilingTableRecipeInput input, boolean layerMode) {
        int[] amounts = new int[input.getSize()];
        int topLeft = findTopLeft(input);
        if (topLeft == -1) return amounts;

        int outputCount = computeOutputCount(input, layerMode);
        if (outputCount <= 0) return amounts;

        for (int slot : regionSlots(topLeft, input.getWidth())) {
            amounts[slot] = isConcrete(input.getStackInSlot(slot)) ? 1
                    : (layerMode ? outputCount : outputCount * ITEMS_PER_SLOT_PER_BLOCK);
        }
        return amounts;
    }

    private static int[] regionSlots(int topLeft, int width) {
        return new int[]{topLeft, topLeft + 1, topLeft + width, topLeft + width + 1};
    }

    private static boolean isConcrete(ItemStack stack) {
        return TileResolver.resolve(stack).isIn(ModTags.Items.CONCRETE);
    }

    @Nullable
    private static Item slotConcrete(ItemStack stack) {
        ItemStack resolved = TileResolver.resolve(stack);
        if (resolved.isIn(ModTags.Items.CONCRETE)) return resolved.getItem();
        return TileColors.concreteForTile(stack.getItem());
    }

    private boolean isValidIngredient(ItemStack stack) {
        return !stack.isEmpty() && slotConcrete(stack) != null;
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

                if (!isValidIngredient(input.getStackInSlot(i0)) ||
                        !isValidIngredient(input.getStackInSlot(i1)) ||
                        !isValidIngredient(input.getStackInSlot(i2)) ||
                        !isValidIngredient(input.getStackInSlot(i3))) {
                    continue;
                }

                // No mixing: the region must be all concrete or all tile items
                boolean concrete = isConcrete(input.getStackInSlot(i0));
                if (isConcrete(input.getStackInSlot(i1)) != concrete ||
                        isConcrete(input.getStackInSlot(i2)) != concrete ||
                        isConcrete(input.getStackInSlot(i3)) != concrete) {
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
