package com.proctoredgames.tiled;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import com.proctoredgames.tiled.item.ModItemGroups;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tiled implements ModInitializer {
	public static final String MOD_ID = "tiled";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
//		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModRecipeSerializers.register();
		ModDataComponentTypes.registerDataComponentTypes();
	}
}