package com.proctoredgames.tiled.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.RecipeInput;

public class TilingTableRecipeInput implements RecipeInput {
    public static final TilingTableRecipeInput EMPTY = new TilingTableRecipeInput(0, 0, List.of());
    private final int width;
    private final int height;
    private final List<ItemStack> stacks;
    private final RecipeMatcher matcher = new RecipeMatcher();
    private final int stackCount;

    public TilingTableRecipeInput(int width, int height, List<ItemStack> stacks) {
        this.width = width;
        this.height = height;
        this.stacks = stacks;
        int i = 0;

        for (ItemStack itemStack : stacks) {
            if (!itemStack.isEmpty()) {
                i++;
                this.matcher.addInput(itemStack, 1);
            }
        }

        this.stackCount = i;
    }

    public static TilingTableRecipeInput create(int width, int height, List<ItemStack> stacks) {
        return createPositioned(width, height, stacks).input();
    }

    public static TilingTableRecipeInput.Positioned createPositioned(int width, int height, List<ItemStack> stacks) {
        if (width != 0 && height != 0) {
            int i = width - 1;
            int j = 0;
            int k = height - 1;
            int l = 0;

            for (int m = 0; m < height; m++) {
                boolean bl = true;

                for (int n = 0; n < width; n++) {
                    ItemStack itemStack = (ItemStack)stacks.get(n + m * width);
                    if (!itemStack.isEmpty()) {
                        i = Math.min(i, n);
                        j = Math.max(j, n);
                        bl = false;
                    }
                }

                if (!bl) {
                    k = Math.min(k, m);
                    l = Math.max(l, m);
                }
            }

            int m = j - i + 1;
            int o = l - k + 1;
            if (m <= 0 || o <= 0) {
                return TilingTableRecipeInput.Positioned.EMPTY;
            } else if (m == width && o == height) {
                return new TilingTableRecipeInput.Positioned(new TilingTableRecipeInput(width, height, stacks), i, k);
            } else {
                List<ItemStack> list = new ArrayList(m * o);

                for (int p = 0; p < o; p++) {
                    for (int q = 0; q < m; q++) {
                        int r = q + i + (p + k) * width;
                        list.add((ItemStack)stacks.get(r));
                    }
                }

                return new TilingTableRecipeInput.Positioned(new TilingTableRecipeInput(m, o, list), i, k);
            }
        } else {
            return TilingTableRecipeInput.Positioned.EMPTY;
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return (ItemStack)this.stacks.get(slot);
    }

    public ItemStack getStackInSlot(int x, int y) {
        return (ItemStack)this.stacks.get(x + y * this.width);
    }

    @Override
    public int getSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stackCount == 0;
    }

    public RecipeMatcher getRecipeMatcher() {
        return this.matcher;
    }

    public List<ItemStack> getStacks() {
        return this.stacks;
    }

    public int getStackCount() {
        return this.stackCount;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return !(o instanceof TilingTableRecipeInput TilingTableRecipeInput)
                    ? false
                    : this.width == TilingTableRecipeInput.width
                    && this.height == TilingTableRecipeInput.height
                    && this.stackCount == TilingTableRecipeInput.stackCount
                    && ItemStack.stacksEqual(this.stacks, TilingTableRecipeInput.stacks);
        }
    }

    public int hashCode() {
        int i = ItemStack.listHashCode(this.stacks);
        i = 31 * i + this.width;
        return 31 * i + this.height;
    }

    public record Positioned(TilingTableRecipeInput input, int left, int top) {
        public static final TilingTableRecipeInput.Positioned EMPTY = new TilingTableRecipeInput.Positioned(TilingTableRecipeInput.EMPTY, 0, 0);
    }
}
