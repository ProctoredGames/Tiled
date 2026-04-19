package com.proctoredgames.tiled.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.recipe.custom.CraftingSmallTileBlockRecipe;
import com.proctoredgames.tiled.recipe.custom.CraftingTileBlockRecipe;
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

    public static final RecipeSerializer<CraftingSmallTileBlockRecipe> CRAFTING_SMALL_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingSmallTileBlockRecipe::new);

    public static final RecipeSerializer<CraftingTileBlockRecipe> CRAFTING_TILE_BLOCK =
            new SpecialRecipeSerializer<>(CraftingTileBlockRecipe::new);

    // Serializer for TilingTableRecipe (used by the tiling table block entity)
    public static final RecipeSerializer<TilingTableRecipe> TILING_TABLE_RECIPE =
            new RecipeSerializer<>() {
                private static final MapCodec<TilingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                        instance.group(
                                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(TilingTableRecipe::inputItem),
                                ItemStack.CODEC.fieldOf("result").forGetter(TilingTableRecipe::output)
                        ).apply(instance, TilingTableRecipe::new)
                );

                private static final PacketCodec<RegistryByteBuf, TilingTableRecipe> PACKET_CODEC =
                        PacketCodec.tuple(
                                Ingredient.PACKET_CODEC, TilingTableRecipe::inputItem,
                                ItemStack.PACKET_CODEC, TilingTableRecipe::output,
                                TilingTableRecipe::new
                        );

                @Override
                public MapCodec<TilingTableRecipe> codec() {
                    return CODEC;
                }

                @Override
                public PacketCodec<RegistryByteBuf, TilingTableRecipe> packetCodec() {
                    return PACKET_CODEC;
                }
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
                Identifier.of(Tiled.MOD_ID, "tiling_table"),
                TILING_TABLE_RECIPE
        );
        Tiled.LOGGER.info("Registering recipe serializers for " + Tiled.MOD_ID);
    }
}