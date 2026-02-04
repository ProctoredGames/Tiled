package com.proctoredgames.tiled.recipe.custom;

import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.recipe.ModRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class CraftingTileBlockRecipe extends SpecialCraftingRecipe {
    public CraftingTileBlockRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (!this.fits(craftingRecipeInput.getWidth(), craftingRecipeInput.getHeight())) {
            return false;
        } else {
            for (int i = 0; i < craftingRecipeInput.getSize(); i++) {
                ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
                switch (i) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        if (!itemStack.isIn(ItemTags.TERRACOTTA)) {
                            return false;
                        }
                        break;
                    case 2:
                    case 4:
                    case 6:
                    default:
                        if (!itemStack.isOf(Items.AIR)) {
                            return false;
                        }
                }
            }

            return true;
        }
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        Tiles tiles = new Tiles(
                craftingRecipeInput.getStackInSlot(1).getItem(),
                craftingRecipeInput.getStackInSlot(3).getItem(),
                craftingRecipeInput.getStackInSlot(5).getItem(),
                craftingRecipeInput.getStackInSlot(7).getItem()
        );
        return TileBlockBE.getStackWith(tiles);
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_TILE_BLOCK;
    }
}
