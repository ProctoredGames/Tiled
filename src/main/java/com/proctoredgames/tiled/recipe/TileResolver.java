package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Optional;

/**
 * Resolves tile block and small tile block ItemStacks to their effective
 * concrete ingredient for recipe matching purposes.
 *
 * A monochrome tile/small tile block (all slots the same concrete color)
 * is treated as a single stack of that concrete block.
 * Mixed-color or empty blocks resolve to themselves (i.e. are not valid concrete).
 */
public class TileResolver {

    /**
     * Returns the effective ItemStack for recipe purposes.
     * If the stack is a monochrome tile block or small tile block,
     * returns a stack of the corresponding concrete item.
     * Otherwise returns the original stack unchanged.
     */
    public static ItemStack resolve(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        if (stack.isOf(ModBlocks.SMALL_TILE_BLOCK.asItem())) {
            SmallTiles tiles = stack.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT);
            Item concrete = getMonochromeItem(tiles.stream());
            if (concrete != null) {
                return new ItemStack(concrete);
            }
        } else if (stack.isOf(ModBlocks.TILE_BLOCK.asItem())) {
            Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
            Item concrete = getMonochromeItem(tiles.stream());
            if (concrete != null) {
                return new ItemStack(concrete);
            }
        }

        return stack;
    }

    /**
     * Returns the single Item if all entries in the list are the same non-air item,
     * or null if the list is mixed, empty, or contains air.
     */
    private static Item getMonochromeItem(List<Item> items) {
        Item found = null;
        for (Item item : items) {
            if (item == Items.AIR) return null;
            if (found == null) {
                found = item;
            } else if (found != item) {
                return null;
            }
        }
        return found;
    }
}