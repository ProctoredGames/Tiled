package com.proctoredgames.tiled.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record TilingTableRecipe(Ingredient inputItem, ItemStack output) implements Recipe<TilingTableRecipeInput> {
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    // read Recipe JSON files --> new TilingTableRecipe

    @Override
    public boolean matches(TilingTableRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(TilingTableRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TILING_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TILING_TABLE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<TilingTableRecipe> {
        public static final MapCodec<TilingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(TilingTableRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(TilingTableRecipe::output)
        ).apply(inst, TilingTableRecipe::new));

        public static final PacketCodec<RegistryByteBuf, TilingTableRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, TilingTableRecipe::inputItem,
                        ItemStack.PACKET_CODEC, TilingTableRecipe::output,
                        TilingTableRecipe::new);

        @Override
        public MapCodec<TilingTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, TilingTableRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}