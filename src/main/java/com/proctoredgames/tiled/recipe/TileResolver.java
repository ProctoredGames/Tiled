package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public class TileResolver {

    public static ItemStack resolve(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        if (stack.isOf(ModBlocks.SMALL_TILE_BLOCK.asItem())) {
            NbtCompound tag = stack.getSubNbt("BlockEntityTag");
            SmallTiles tiles = SmallTiles.fromNbt(tag);
            Item concrete = getMonochromeItem(tiles.stream());
            if (concrete != null) {
                return new ItemStack(concrete);
            }
        } else if (stack.isOf(ModBlocks.TILE_BLOCK.asItem())) {
            NbtCompound tag = stack.getSubNbt("BlockEntityTag");
            Tiles tiles = Tiles.fromNbt(tag);
            Item concrete = getMonochromeItem(tiles.stream());
            if (concrete != null) {
                return new ItemStack(concrete);
            }
        }

        return stack;

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