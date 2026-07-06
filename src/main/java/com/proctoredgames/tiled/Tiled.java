package com.proctoredgames.tiled;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.SmallTileLayerBE;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.TileLayerBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.item.ModItemGroups;
import com.proctoredgames.tiled.item.ModItems;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.screen.ModScreenHandlers;
import com.proctoredgames.tiled.util.ModTags;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tiled implements ModInitializer {
	public static final String MOD_ID = "tiled";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipeSerializers.register();
		ModRecipes.registerRecipes();

		ItemGroupEvents.modifyEntriesEvent(ModItemGroups.TILED_GROUP_KEY)
				.register(entries -> {

					for (RegistryEntry<Item> entry :
							Registries.ITEM.iterateEntries(ModTags.Items.CONCRETE)) {

						Item tile = entry.value();

						SmallTiles tiles = new SmallTiles(
								tile, tile, tile, tile,
								tile, tile, tile, tile,
								tile, tile, tile, tile,
								tile, tile, tile, tile
						);
						ItemStack stack = SmallTileBlockBE.getStackWith(tiles);

						entries.add(stack);
					}

					for (RegistryEntry<Item> entry :
							Registries.ITEM.iterateEntries(ModTags.Items.CONCRETE)) {

						Item tile = entry.value();

						Tiles tiles = new Tiles(tile, tile, tile, tile);
						ItemStack stack = TileBlockBE.getStackWith(tiles);

						entries.add(stack);
					}

					for (RegistryEntry<Item> entry :
							Registries.ITEM.iterateEntries(ModTags.Items.CONCRETE)) {

						Item tile = entry.value();

						SmallTiles tiles = new SmallTiles(
								tile, tile, tile, tile,
								tile, tile, tile, tile,
								tile, tile, tile, tile,
								tile, tile, tile, tile
						);
						ItemStack stack = SmallTileLayerBE.getStackWith(tiles);

						entries.add(stack);
					}

					for (RegistryEntry<Item> entry :
							Registries.ITEM.iterateEntries(ModTags.Items.CONCRETE)) {

						Item tile = entry.value();

						Tiles tiles = new Tiles(tile, tile, tile, tile);
						ItemStack stack = TileLayerBE.getStackWith(tiles);

						entries.add(stack);
					}
				});

	}
}