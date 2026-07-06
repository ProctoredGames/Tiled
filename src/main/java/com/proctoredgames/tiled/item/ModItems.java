package com.proctoredgames.tiled.item;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.item.custom.TileTrowelItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SMALL_WHITE_CONCRETE_TILE = registerItem("small_white_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_ORANGE_CONCRETE_TILE = registerItem("small_orange_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_MAGENTA_CONCRETE_TILE = registerItem("small_magenta_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIGHT_BLUE_CONCRETE_TILE = registerItem("small_light_blue_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_YELLOW_CONCRETE_TILE = registerItem("small_yellow_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIME_CONCRETE_TILE = registerItem("small_lime_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_PINK_CONCRETE_TILE = registerItem("small_pink_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_GRAY_CONCRETE_TILE = registerItem("small_gray_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_LIGHT_GRAY_CONCRETE_TILE = registerItem("small_light_gray_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_CYAN_CONCRETE_TILE = registerItem("small_cyan_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_PURPLE_CONCRETE_TILE = registerItem("small_purple_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BLUE_CONCRETE_TILE = registerItem("small_blue_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BROWN_CONCRETE_TILE = registerItem("small_brown_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_GREEN_CONCRETE_TILE = registerItem("small_green_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_RED_CONCRETE_TILE = registerItem("small_red_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item SMALL_BLACK_CONCRETE_TILE = registerItem("small_black_concrete_tile", new Item(new Item.Settings().maxCount(96)));

    public static final Item WHITE_CONCRETE_TILE = registerItem("white_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item ORANGE_CONCRETE_TILE = registerItem("orange_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item MAGENTA_CONCRETE_TILE = registerItem("magenta_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIGHT_BLUE_CONCRETE_TILE = registerItem("light_blue_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item YELLOW_CONCRETE_TILE = registerItem("yellow_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIME_CONCRETE_TILE = registerItem("lime_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item PINK_CONCRETE_TILE = registerItem("pink_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item GRAY_CONCRETE_TILE = registerItem("gray_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item LIGHT_GRAY_CONCRETE_TILE = registerItem("light_gray_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item CYAN_CONCRETE_TILE = registerItem("cyan_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item PURPLE_CONCRETE_TILE = registerItem("purple_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BLUE_CONCRETE_TILE = registerItem("blue_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BROWN_CONCRETE_TILE = registerItem("brown_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item GREEN_CONCRETE_TILE = registerItem("green_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item RED_CONCRETE_TILE = registerItem("red_concrete_tile", new Item(new Item.Settings().maxCount(96)));
    public static final Item BLACK_CONCRETE_TILE = registerItem("black_concrete_tile", new Item(new Item.Settings().maxCount(96)));

    public static final Item TILE_TROWEL = registerItem("tile_trowel", new TileTrowelItem(new Item.Settings().maxDamage(250)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Tiled.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Tiled.LOGGER.info("Registering Mod Items for " + Tiled.MOD_ID);
    }
}