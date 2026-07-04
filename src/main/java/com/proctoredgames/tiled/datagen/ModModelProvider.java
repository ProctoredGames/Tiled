package com.proctoredgames.tiled.datagen;

import com.proctoredgames.tiled.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Placeholder textures while the real tile item art is being reworked:
        // every tile item uses its color of concrete
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_WHITE_TILE, "white");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_LIGHT_GRAY_TILE, "light_gray");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_GRAY_TILE, "gray");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_BLACK_TILE, "black");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_BROWN_TILE, "brown");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_RED_TILE, "red");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_ORANGE_TILE, "orange");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_YELLOW_TILE, "yellow");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_LIME_TILE, "lime");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_GREEN_TILE, "green");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_CYAN_TILE, "cyan");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_LIGHT_BLUE_TILE, "light_blue");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_BLUE_TILE, "blue");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_PURPLE_TILE, "purple");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_MAGENTA_TILE, "magenta");
        registerConcreteTextured(itemModelGenerator, ModItems.SMALL_PINK_TILE, "pink");

        registerConcreteTextured(itemModelGenerator, ModItems.WHITE_TILE, "white");
        registerConcreteTextured(itemModelGenerator, ModItems.LIGHT_GRAY_TILE, "light_gray");
        registerConcreteTextured(itemModelGenerator, ModItems.GRAY_TILE, "gray");
        registerConcreteTextured(itemModelGenerator, ModItems.BLACK_TILE, "black");
        registerConcreteTextured(itemModelGenerator, ModItems.BROWN_TILE, "brown");
        registerConcreteTextured(itemModelGenerator, ModItems.RED_TILE, "red");
        registerConcreteTextured(itemModelGenerator, ModItems.ORANGE_TILE, "orange");
        registerConcreteTextured(itemModelGenerator, ModItems.YELLOW_TILE, "yellow");
        registerConcreteTextured(itemModelGenerator, ModItems.LIME_TILE, "lime");
        registerConcreteTextured(itemModelGenerator, ModItems.GREEN_TILE, "green");
        registerConcreteTextured(itemModelGenerator, ModItems.CYAN_TILE, "cyan");
        registerConcreteTextured(itemModelGenerator, ModItems.LIGHT_BLUE_TILE, "light_blue");
        registerConcreteTextured(itemModelGenerator, ModItems.BLUE_TILE, "blue");
        registerConcreteTextured(itemModelGenerator, ModItems.PURPLE_TILE, "purple");
        registerConcreteTextured(itemModelGenerator, ModItems.MAGENTA_TILE, "magenta");
        registerConcreteTextured(itemModelGenerator, ModItems.PINK_TILE, "pink");

        itemModelGenerator.register(ModItems.TILE_TROWEL, Models.HANDHELD);
    }

    private static void registerConcreteTextured(ItemModelGenerator itemModelGenerator, Item item, String color) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item),
                TextureMap.layer0(Identifier.ofVanilla("block/" + color + "_concrete")),
                itemModelGenerator.writer);
    }
}
