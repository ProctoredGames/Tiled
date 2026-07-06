package com.proctoredgames.tiled.recipe;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.recipe.custom.CraftingSmallTileBlock;
import com.proctoredgames.tiled.recipe.custom.CraftingTileBlock;
import com.proctoredgames.tiled.recipe.custom.CraftingTileItems;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileBlockRecipe;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileItemRecipe;
import com.proctoredgames.tiled.recipe.custom.TilingTableTileBlockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ModRecipeSerializers {

    public static final RecipeSerializer<CraftingSmallTileBlock> CRAFTING_SMALL_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingSmallTileBlock::new);

    public static final RecipeSerializer<CraftingTileBlock> CRAFTING_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingTileBlock::new);

    public static final RecipeSerializer<CraftingTileItems> CRAFTING_TILE_ITEMS =
            new SpecialRecipeSerializer<>(CraftingTileItems::new);

    public static final RecipeSerializer<TilingTableSmallTileBlockRecipe> TILING_TABLE_SMALL_TILE_BLOCK_RECIPE =
            new RecipeSerializer<>() {
                @Override
                public TilingTableSmallTileBlockRecipe read(Identifier id, com.google.gson.JsonObject json) {
                    Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
                    ItemStack result = itemStackFromJson(json.getAsJsonObject("result"));
                    return new TilingTableSmallTileBlockRecipe(id, ingredient, result);
                }

                @Override
                public TilingTableSmallTileBlockRecipe read(Identifier id, PacketByteBuf buf) {
                    Ingredient ingredient = Ingredient.fromPacket(buf);
                    ItemStack result = buf.readItemStack();
                    return new TilingTableSmallTileBlockRecipe(id, ingredient, result);
                }

                @Override
                public void write(PacketByteBuf buf, TilingTableSmallTileBlockRecipe recipe) {
                    recipe.inputItem().write(buf);
                    buf.writeItemStack(recipe.output());
                }
            };

    public static final RecipeSerializer<TilingTableTileBlockRecipe> TILING_TABLE_TILE_BLOCK_RECIPE =
            new RecipeSerializer<>() {
                @Override
                public TilingTableTileBlockRecipe read(Identifier id, com.google.gson.JsonObject json) {
                    Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
                    ItemStack result = itemStackFromJson(json.getAsJsonObject("result"));
                    return new TilingTableTileBlockRecipe(id, ingredient, result);
                }

                @Override
                public TilingTableTileBlockRecipe read(Identifier id, PacketByteBuf buf) {
                    Ingredient ingredient = Ingredient.fromPacket(buf);
                    ItemStack result = buf.readItemStack();
                    return new TilingTableTileBlockRecipe(id, ingredient, result);
                }

                @Override
                public void write(PacketByteBuf buf, TilingTableTileBlockRecipe recipe) {
                    recipe.inputItem().write(buf);
                    buf.writeItemStack(recipe.output());
                }
            };

    public static final RecipeSerializer<TilingTableSmallTileItemRecipe> TILING_TABLE_SMALL_TILE_ITEM_RECIPE =
            new RecipeSerializer<>() {
                @Override
                public TilingTableSmallTileItemRecipe read(Identifier id, com.google.gson.JsonObject json) {
                    Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
                    ItemStack result = itemStackFromJson(json.getAsJsonObject("result"));
                    return new TilingTableSmallTileItemRecipe(id, ingredient, result);
                }

                @Override
                public TilingTableSmallTileItemRecipe read(Identifier id, PacketByteBuf buf) {
                    Ingredient ingredient = Ingredient.fromPacket(buf);
                    ItemStack result = buf.readItemStack();
                    return new TilingTableSmallTileItemRecipe(id, ingredient, result);
                }

                @Override
                public void write(PacketByteBuf buf, TilingTableSmallTileItemRecipe recipe) {
                    recipe.inputItem().write(buf);
                    buf.writeItemStack(recipe.output());
                }
            };

    private static ItemStack itemStackFromJson(com.google.gson.JsonObject json) {
        String itemId = JsonHelper.getString(json, "item");
        net.minecraft.item.Item item = Registries.ITEM.get(new Identifier(itemId));
        int count = JsonHelper.getInt(json, "count", 1);
        return new ItemStack(item, count);
    }

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "crafting_small_tile_block"), CRAFTING_SMALL_TILE_BLOCK);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "crafting_tile_block"), CRAFTING_TILE_BLOCK);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "crafting_tile_items"), CRAFTING_TILE_ITEMS);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "tiling_table_small_tile_block_recipe"), TILING_TABLE_SMALL_TILE_BLOCK_RECIPE);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "tiling_table_tile_block_recipe"), TILING_TABLE_TILE_BLOCK_RECIPE);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Tiled.MOD_ID, "tiling_table_small_tile_item_recipe"), TILING_TABLE_SMALL_TILE_ITEM_RECIPE);
        Tiled.LOGGER.info("Registering recipe serializers for " + Tiled.MOD_ID);
    }
}