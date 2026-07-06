package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.TileResolver;
import com.proctoredgames.tiled.util.TileColors;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// Uncrafts a single solid color tile block or layer (regular or small)
// into tile items of that color: 24 per block, 4 per layer
public class CraftingTileItems extends SpecialCraftingRecipe {

    public static final int TILES_PER_BLOCK = 24;
    public static final int TILES_PER_LAYER = 4;

    public CraftingTileItems(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory input, World world) {
        return findInputSlot(input) != -1;
    }

    @Override
    public ItemStack craft(RecipeInputInventory input, DynamicRegistryManager registryManager) {
        int slot = findInputSlot(input);
        if (slot == -1) return ItemStack.EMPTY;

        ItemStack stack = input.getStack(slot);
        Item concrete = TileResolver.solidConcrete(stack);
        if (concrete == null) return ItemStack.EMPTY;

        Item tile = TileColors.tileForConcrete(concrete);
        if (tile == null) return ItemStack.EMPTY;

        return new ItemStack(tile, TileResolver.isLayerItem(stack) ? TILES_PER_LAYER : TILES_PER_BLOCK);
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    // Matches exactly one solid color tile block or layer alone in the grid
    private int findInputSlot(RecipeInputInventory input) {
        int found = -1;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStack(i);
            if (stack.isEmpty()) continue;
            if (found != -1) return -1;
            Item concrete = TileResolver.solidConcrete(stack);
            if (concrete == null || TileColors.tileForConcrete(concrete) == null) return -1;
            found = i;
        }
        return found;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 1 && height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_TILE_ITEMS;
    }
}
