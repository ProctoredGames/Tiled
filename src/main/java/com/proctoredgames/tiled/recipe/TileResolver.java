package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileResolver {

    public static ItemStack resolve(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        if (stack.isOf(ModBlocks.SMALL_TILE_BLOCK.asItem()) || stack.isOf(ModBlocks.TILE_BLOCK.asItem())) {
            Item concrete = solidConcrete(stack);
            if (concrete != null) {
                return new ItemStack(concrete);
            }
        }

        return stack;

    }

    // Returns the concrete color of a solid color tile block or layer
    // (regular or small), or null for anything else
    @Nullable
    public static Item solidConcrete(ItemStack stack) {
        if (stack.isOf(ModBlocks.SMALL_TILE_BLOCK.asItem()) || stack.isOf(ModBlocks.SMALL_TILE_LAYER.asItem())) {
            SmallTiles tiles = SmallTiles.fromNbt(stack.getSubNbt("BlockEntityTag"));
            return getMonochromeItem(tiles.stream());
        }
        if (stack.isOf(ModBlocks.TILE_BLOCK.asItem()) || stack.isOf(ModBlocks.TILE_LAYER.asItem())) {
            Tiles tiles = Tiles.fromNbt(stack.getSubNbt("BlockEntityTag"));
            return getMonochromeItem(tiles.stream());
        }
        return null;
    }

    public static boolean isLayerItem(ItemStack stack) {
        return stack.isOf(ModBlocks.TILE_LAYER.asItem()) || stack.isOf(ModBlocks.SMALL_TILE_LAYER.asItem());
    }

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