package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.item.custom.TileTrowelItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SMALL_WHITE_TILE = registerItem("small_white_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_ORANGE_TILE = registerItem("small_orange_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_MAGENTA_TILE = registerItem("small_magenta_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIGHT_BLUE_TILE = registerItem("small_light_blue_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_YELLOW_TILE = registerItem("small_yellow_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIME_TILE = registerItem("small_lime_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_PINK_TILE = registerItem("small_pink_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_GRAY_TILE = registerItem("small_gray_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIGHT_GRAY_TILE = registerItem("small_light_gray_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_CYAN_TILE = registerItem("small_cyan_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_PURPLE_TILE = registerItem("small_purple_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BLUE_TILE = registerItem("small_blue_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BROWN_TILE = registerItem("small_brown_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_GREEN_TILE = registerItem("small_green_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_RED_TILE = registerItem("small_red_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BLACK_TILE = registerItem("small_black_tile", new Item(new Item.Settings().maxCount(96)));

    public static final Item WHITE_TILE = registerItem("white_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item ORANGE_TILE = registerItem("orange_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item MAGENTA_TILE = registerItem("magenta_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIGHT_BLUE_TILE = registerItem("light_blue_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item YELLOW_TILE = registerItem("yellow_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIME_TILE = registerItem("lime_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item PINK_TILE = registerItem("pink_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item GRAY_TILE = registerItem("gray_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIGHT_GRAY_TILE = registerItem("light_gray_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item CYAN_TILE = registerItem("cyan_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item PURPLE_TILE = registerItem("purple_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BLUE_TILE = registerItem("blue_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BROWN_TILE = registerItem("brown_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item GREEN_TILE = registerItem("green_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item RED_TILE = registerItem("red_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BLACK_TILE = registerItem("black_tile", new Item(new Item.Settings().maxCount(96)));

    public static final Item TILE_TROWEL = registerItem("tile_trowel", new TileTrowelItem(new Item.Settings().maxDamage(250)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Tiled.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Tiled.LOGGER.info("Registering Mod Items for " + Tiled.MOD_ID);
    }
}
