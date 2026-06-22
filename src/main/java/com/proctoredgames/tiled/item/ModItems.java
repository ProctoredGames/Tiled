package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SMALL_WHITE_TILES = registerItem("small_white_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_ORANGE_TILES = registerItem("small_orange_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_MAGENTA_TILES = registerItem("small_magenta_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_LIGHT_BLUE_TILES = registerItem("small_light_blue_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_YELLOW_TILES = registerItem("small_yellow_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_LIME_TILES = registerItem("small_lime_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_PINK_TILES = registerItem("small_pink_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_GRAY_TILES = registerItem("small_gray_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_LIGHT_GRAY_TILES = registerItem("small_light_gray_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_CYAN_TILES = registerItem("small_cyan_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_PURPLE_TILES = registerItem("small_purple_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_BLUE_TILES = registerItem("small_blue_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_BROWN_TILES = registerItem("small_brown_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_GREEN_TILES = registerItem("small_green_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_RED_TILES = registerItem("small_red_tiles", new Item(new Item.Settings()));
    public static final Item SMALL_BLACK_TILES = registerItem("small_black_tiles", new Item(new Item.Settings()));

    public static final Item WHITE_TILES = registerItem("white_tiles", new Item(new Item.Settings()));
    public static final Item ORANGE_TILES = registerItem("orange_tiles", new Item(new Item.Settings()));
    public static final Item MAGENTA_TILES = registerItem("magenta_tiles", new Item(new Item.Settings()));
    public static final Item LIGHT_BLUE_TILES = registerItem("light_blue_tiles", new Item(new Item.Settings()));
    public static final Item YELLOW_TILES = registerItem("yellow_tiles", new Item(new Item.Settings()));
    public static final Item LIME_TILES = registerItem("lime_tiles", new Item(new Item.Settings()));
    public static final Item PINK_TILES = registerItem("pink_tiles", new Item(new Item.Settings()));
    public static final Item GRAY_TILES = registerItem("gray_tiles", new Item(new Item.Settings()));
    public static final Item LIGHT_GRAY_TILES = registerItem("light_gray_tiles", new Item(new Item.Settings()));
    public static final Item CYAN_TILES = registerItem("cyan_tiles", new Item(new Item.Settings()));
    public static final Item PURPLE_TILES = registerItem("purple_tiles", new Item(new Item.Settings()));
    public static final Item BLUE_TILES = registerItem("blue_tiles", new Item(new Item.Settings()));
    public static final Item BROWN_TILES = registerItem("brown_tiles", new Item(new Item.Settings()));
    public static final Item GREEN_TILES = registerItem("green_tiles", new Item(new Item.Settings()));
    public static final Item RED_TILES = registerItem("red_tiles", new Item(new Item.Settings()));
    public static final Item BLACK_TILES = registerItem("black_tiles", new Item(new Item.Settings()));

    public static final Item TILE_TROWEL = registerItem("tile_trowel", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Tiled.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Tiled.LOGGER.info("Registering Mod Items for " + Tiled.MOD_ID);
    }
}
