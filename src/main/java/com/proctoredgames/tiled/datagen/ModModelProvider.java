package com.proctoredgames.tiled.datagen;

import com.proctoredgames.tiled.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SMALL_WHITE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIGHT_GRAY_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_GRAY_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BLACK_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BROWN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_RED_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_ORANGE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_YELLOW_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIME_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_GREEN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_CYAN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_LIGHT_BLUE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_BLUE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_PURPLE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_MAGENTA_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SMALL_PINK_CONCRETE_TILE, Models.GENERATED);

        itemModelGenerator.register(ModItems.WHITE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIGHT_GRAY_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GRAY_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLACK_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BROWN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ORANGE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.YELLOW_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIME_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GREEN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CYAN_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIGHT_BLUE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PURPLE_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGENTA_CONCRETE_TILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PINK_CONCRETE_TILE, Models.GENERATED);

        itemModelGenerator.register(ModItems.TILE_TROWEL, Models.HANDHELD);
    }
}
