package com.proctoredgames.tiled.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.recipe.custom.CraftingSmallTileBlock;
import com.proctoredgames.tiled.recipe.custom.CraftingTileBlock;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileBlockRecipe;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileItemRecipe;
import com.proctoredgames.tiled.recipe.custom.TilingTableTileBlockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {

    public static final RecipeSerializer<CraftingSmallTileBlock> CRAFTING_SMALL_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingSmallTileBlock::new);

    public static final RecipeSerializer<CraftingTileBlock> CRAFTING_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingTileBlock::new);

    public static final RecipeSerializer<TilingTableSmallTileBlockRecipe> TILING_TABLE_SMALL_TILE_BLOCK_RECIPE =
            new RecipeSerializer<>() {
                private static final MapCodec<TilingTableSmallTileBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                        instance.group(
                                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(TilingTableSmallTileBlockRecipe::inputItem),
                                ItemStack.CODEC.fieldOf("result").forGetter(TilingTableSmallTileBlockRecipe::output)
                        ).apply(instance, TilingTableSmallTileBlockRecipe::new)
                );
                private static final PacketCodec<RegistryByteBuf, TilingTableSmallTileBlockRecipe> PACKET_CODEC =
                        PacketCodec.tuple(
                                Ingredient.PACKET_CODEC, TilingTableSmallTileBlockRecipe::inputItem,
                                ItemStack.PACKET_CODEC, TilingTableSmallTileBlockRecipe::output,
                                TilingTableSmallTileBlockRecipe::new
                        );

                @Override
                public MapCodec<TilingTableSmallTileBlockRecipe> codec() { return CODEC; }

                @Override
                public PacketCodec<RegistryByteBuf, TilingTableSmallTileBlockRecipe> packetCodec() { return PACKET_CODEC; }
            };

    public static final RecipeSerializer<TilingTableSmallTileItemRecipe> TILING_TABLE_SMALL_TILE_ITEM_RECIPE =
            new RecipeSerializer<>() {
                private static final MapCodec<TilingTableSmallTileItemRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                        instance.group(
                                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(TilingTableSmallTileItemRecipe::inputItem),
                                ItemStack.CODEC.fieldOf("result").forGetter(TilingTableSmallTileItemRecipe::output)
                        ).apply(instance, TilingTableSmallTileItemRecipe::new)
                );
                private static final PacketCodec<RegistryByteBuf, TilingTableSmallTileItemRecipe> PACKET_CODEC =
                        PacketCodec.tuple(
                                Ingredient.PACKET_CODEC, TilingTableSmallTileItemRecipe::inputItem,
                                ItemStack.PACKET_CODEC, TilingTableSmallTileItemRecipe::output,
                                TilingTableSmallTileItemRecipe::new
                        );

                @Override
                public MapCodec<TilingTableSmallTileItemRecipe> codec() { return CODEC; }

                @Override
                public PacketCodec<RegistryByteBuf, TilingTableSmallTileItemRecipe> packetCodec() { return PACKET_CODEC; }
            };

    public static final RecipeSerializer<TilingTableTileBlockRecipe> TILING_TABLE_TILE_BLOCK_RECIPE =
            new RecipeSerializer<>() {
                private static final MapCodec<TilingTableTileBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                        instance.group(
                                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(TilingTableTileBlockRecipe::inputItem),
                                ItemStack.CODEC.fieldOf("result").forGetter(TilingTableTileBlockRecipe::output)
                        ).apply(instance, TilingTableTileBlockRecipe::new)
                );
                private static final PacketCodec<RegistryByteBuf, TilingTableTileBlockRecipe> PACKET_CODEC =
                        PacketCodec.tuple(
                                Ingredient.PACKET_CODEC, TilingTableTileBlockRecipe::inputItem,
                                ItemStack.PACKET_CODEC, TilingTableTileBlockRecipe::output,
                                TilingTableTileBlockRecipe::new
                        );

                @Override
                public MapCodec<TilingTableTileBlockRecipe> codec() { return CODEC; }

                @Override
                public PacketCodec<RegistryByteBuf, TilingTableTileBlockRecipe> packetCodec() { return PACKET_CODEC; }
            };

    public static void register() {
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "crafting_small_tile_block"),
                CRAFTING_SMALL_TILE_BLOCK
        );
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "crafting_tile_block"),
                CRAFTING_TILE_BLOCK
        );
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "tiling_table_small_tile_block_recipe"),
                TILING_TABLE_SMALL_TILE_BLOCK_RECIPE
        );
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "tiling_table_tile_block_recipe"),
                TILING_TABLE_TILE_BLOCK_RECIPE
        );
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(Tiled.MOD_ID, "tiling_table_small_tile_item_recipe"),
                TILING_TABLE_SMALL_TILE_ITEM_RECIPE
        );
        Tiled.LOGGER.info("Registering recipe serializers for " + Tiled.MOD_ID);
    }
}