package com.proctoredgames.tiled.datagen;

import com.proctoredgames.tiled.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SMALL_WHITE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIGHT_GRAY_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_GRAY_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BLACK_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BROWN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_RED_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_ORANGE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_YELLOW_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIME_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_GREEN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_CYAN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIGHT_BLUE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BLUE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_PURPLE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_MAGENTA_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_PINK_TILE, Models.GENERATED);

        itemModelGenerator.register(ModItems.WHITE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIGHT_GRAY_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GRAY_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLACK_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BROWN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ORANGE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.YELLOW_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIME_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GREEN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CYAN_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIGHT_BLUE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PURPLE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGENTA_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PINK_TILE, Models.GENERATED);

        itemModelGenerator.register(ModItems.TILE_TROWEL, Models.HANDHELD);
    }
}