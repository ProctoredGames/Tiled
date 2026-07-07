package com.proctoredgames.tiled.recipe.custom;

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

public record TilingTableSmallTileItemRecipe(Ingredient inputItem, ItemStack output) implements Recipe<TilingTableRecipeInput> {

    public static final int OUTPUT_COUNT = 96;
    public static final int LAYER_OUTPUT_COUNT = 16;

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(TilingTableRecipeInput input, World world) {
        return findInputSlot(input) != -1;
    }

    @Override
    public ItemStack craft(TilingTableRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        int slot = findInputSlot(input);
        if (slot == -1) return ItemStack.EMPTY;

        ItemStack stack = input.getStackInSlot(slot);
        Item smallTile = TileColors.smallTileForConcrete(concreteFor(stack));
        if (smallTile == null) return ItemStack.EMPTY;

        return new ItemStack(smallTile, TileResolver.isLayerItem(stack) ? LAYER_OUTPUT_COUNT : OUTPUT_COUNT);
    }

    public int[] computeConsumption(TilingTableRecipeInput input) {
        int[] amounts = new int[input.getSize()];
        int slot = findInputSlot(input);
        if (slot != -1) {
            amounts[slot] = 1;
        }
        return amounts;
    }

    // Matches exactly one stack alone in the grid that is either concrete or
    // a solid color tile block/layer, so this recipe never competes with the
    // 4x4 small tile block recipe
    private int findInputSlot(TilingTableRecipeInput input) {
        int found = -1;
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (found != -1 || concreteFor(stack) == null) return -1;
            found = i;
        }
        return found;
    }

    // The concrete color an input stack provides: the item itself for
    // concrete, the tile color for solid color tile blocks and layers
    @Nullable
    private static Item concreteFor(ItemStack stack) {
        if (stack.isIn(ModTags.Items.CONCRETE)) return stack.getItem();
        return TileResolver.solidConcrete(stack);
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 1 && height >= 1;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.TILING_TABLE_SMALL_TILE_ITEM_RECIPE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TILING_TABLE_SMALL_TILE_ITEM_TYPE;
    }
}
