package com.proctoredgames.tiled.util;

import com.proctoredgames.tiled.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TileColors {

    private static final Map<Item, Item> CONCRETE_TO_TILE = new HashMap<>();
    private static final Map<Item, Item> CONCRETE_TO_SMALL_TILE = new HashMap<>();
    private static final Map<Item, Item> TILE_TO_CONCRETE = new HashMap<>();
    private static final Map<Item, Item> SMALL_TILE_TO_CONCRETE = new HashMap<>();

    static {
        register(Items.WHITE_CONCRETE, ModItems.WHITE_TILE, ModItems.SMALL_WHITE_TILE);
        register(Items.ORANGE_CONCRETE, ModItems.ORANGE_TILE, ModItems.SMALL_ORANGE_TILE);
        register(Items.MAGENTA_CONCRETE, ModItems.MAGENTA_TILE, ModItems.SMALL_MAGENTA_TILE);
        register(Items.LIGHT_BLUE_CONCRETE, ModItems.LIGHT_BLUE_TILE, ModItems.SMALL_LIGHT_BLUE_TILE);
        register(Items.YELLOW_CONCRETE, ModItems.YELLOW_TILE, ModItems.SMALL_YELLOW_TILE);
        register(Items.LIME_CONCRETE, ModItems.LIME_TILE, ModItems.SMALL_LIME_TILE);
        register(Items.PINK_CONCRETE, ModItems.PINK_TILE, ModItems.SMALL_PINK_TILE);
        register(Items.GRAY_CONCRETE, ModItems.GRAY_TILE, ModItems.SMALL_GRAY_TILE);
        register(Items.LIGHT_GRAY_CONCRETE, ModItems.LIGHT_GRAY_TILE, ModItems.SMALL_LIGHT_GRAY_TILE);
        register(Items.CYAN_CONCRETE, ModItems.CYAN_TILE, ModItems.SMALL_CYAN_TILE);
        register(Items.PURPLE_CONCRETE, ModItems.PURPLE_TILE, ModItems.SMALL_PURPLE_TILE);
        register(Items.BLUE_CONCRETE, ModItems.BLUE_TILE, ModItems.SMALL_BLUE_TILE);
        register(Items.BROWN_CONCRETE, ModItems.BROWN_TILE, ModItems.SMALL_BROWN_TILE);
        register(Items.GREEN_CONCRETE, ModItems.GREEN_TILE, ModItems.SMALL_GREEN_TILE);
        register(Items.RED_CONCRETE, ModItems.RED_TILE, ModItems.SMALL_RED_TILE);
        register(Items.BLACK_CONCRETE, ModItems.BLACK_TILE, ModItems.SMALL_BLACK_TILE);
    }

    private static void register(Item concrete, Item tile, Item smallTile) {
        CONCRETE_TO_TILE.put(concrete, tile);
        CONCRETE_TO_SMALL_TILE.put(concrete, smallTile);
        TILE_TO_CONCRETE.put(tile, concrete);
        SMALL_TILE_TO_CONCRETE.put(smallTile, concrete);
    }

    @Nullable
    public static Item tileForConcrete(Item concrete) {
        return CONCRETE_TO_TILE.get(concrete);
    }

    @Nullable
    public static Item smallTileForConcrete(Item concrete) {
        return CONCRETE_TO_SMALL_TILE.get(concrete);
    }

    @Nullable
    public static Item concreteForTile(Item tile) {
        return TILE_TO_CONCRETE.get(tile);
    }

    @Nullable
    public static Item concreteForSmallTile(Item smallTile) {
        return SMALL_TILE_TO_CONCRETE.get(smallTile);
    }

    public static boolean isTileItem(Item item) {
        return TILE_TO_CONCRETE.containsKey(item);
    }

    public static boolean isSmallTileItem(Item item) {
        return SMALL_TILE_TO_CONCRETE.containsKey(item);
    }
}
