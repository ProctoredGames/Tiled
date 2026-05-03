package com.proctoredgames.tiled.datagen;

import com.proctoredgames.tiled.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

// 1.20.1: FabricBlockLootTableProvider constructor takes only FabricDataOutput, no RegistryWrapper
public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.TILING_TABLE);
    }
}