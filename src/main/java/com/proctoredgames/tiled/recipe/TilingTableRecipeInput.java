package com.proctoredgames.tiled.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

// 1.20.1: getFirstMatch is typed <C extends Inventory, T extends Recipe<C>>
// TilingTableRecipeInput only needs to implement Inventory — it is not used with SpecialCraftingRecipe
public record TilingTableRecipeInput(DefaultedList<ItemStack> input) implements Inventory {

    public int getWidth() {
        return 4;
    }

    public int getHeight() {
        return 4;
    }

    @Override
    public int size() {
        return input.size();
    }

    @Override
    public boolean isEmpty() {
        return input.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return input.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return input.get(slot).split(amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = input.get(slot);
        input.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        input.set(slot, stack);
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        input.replaceAll(s -> ItemStack.EMPTY);
    }
}