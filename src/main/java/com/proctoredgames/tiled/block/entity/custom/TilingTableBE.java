package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ImplementedInventory;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileBlockRecipe;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileItemRecipe;
import com.proctoredgames.tiled.recipe.TilingTableRecipeInput;
import com.proctoredgames.tiled.recipe.custom.TilingTableTileBlockRecipe;
import com.proctoredgames.tiled.screen.custom.TilingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TilingTableBE extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos> {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(17, ItemStack.EMPTY);

    public static final int OUTPUT_SLOT = 16;

    protected final PropertyDelegate propertyDelegate;

    public static final int LAYER_MODE_PROPERTY = 0;

    private boolean layerMode = false;

    public TilingTableBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILING_TABLE_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return index == LAYER_MODE_PROPERTY && TilingTableBE.this.layerMode ? 1 : 0;
            }
            @Override
            public void set(int index, int value) {
                if (index == LAYER_MODE_PROPERTY) {
                    TilingTableBE.this.layerMode = value != 0;
                }
            }
            @Override
            public int size() { return 17; }
        };
    }

    public boolean isLayerMode() {
        return this.layerMode;
    }

    public void toggleLayerMode(World world) {
        this.layerMode = !this.layerMode;
        markDirty();
        updateResult(world);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.tiled.tiling_table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TilingTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putBoolean("layer_mode", layerMode);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        layerMode = nbt.getBoolean("layer_mode");
        super.readNbt(nbt, registryLookup);
    }

    public void updateResult(World world) {
        TilingTableRecipeInput recipeInput = new TilingTableRecipeInput(getInputInventory());
        ItemStack result = ItemStack.EMPTY;

        // Check the single-concrete item recipe, then the small tile block recipe (4x4), then the tile block recipe (2x2)
        Optional<RecipeEntry<TilingTableSmallTileItemRecipe>> smallTileItemRecipe = getSmallTileItemRecipe();
        Optional<RecipeEntry<TilingTableSmallTileBlockRecipe>> smallTileRecipe = getSmallTileRecipe();
        if (smallTileItemRecipe.isPresent()) {
            result = smallTileItemRecipe.get().value().craft(recipeInput, world.getRegistryManager());
        } else if (smallTileRecipe.isPresent()) {
            result = smallTileRecipe.get().value().craft(recipeInput, world.getRegistryManager());
        } else {
            Optional<RecipeEntry<TilingTableTileBlockRecipe>> tileRecipe = getTileBlockRecipe();
            if (tileRecipe.isPresent()) {
                result = tileRecipe.get().value().craft(recipeInput, world.getRegistryManager());
            }
        }

        if (layerMode) {
            result = toLayerStack(result);
        }

        inventory.set(OUTPUT_SLOT, result);
        markDirty();
    }

    // In layer mode, block outputs become their glow-lichen-style layer variants with the same pattern and count
    private static ItemStack toLayerStack(ItemStack result) {
        if (result.isOf(ModBlocks.SMALL_TILE_BLOCK.asItem())) {
            ItemStack converted = new ItemStack(ModBlocks.SMALL_TILES, result.getCount());
            converted.set(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES,
                    result.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT));
            return converted;
        }
        if (result.isOf(ModBlocks.TILE_BLOCK.asItem())) {
            ItemStack converted = new ItemStack(ModBlocks.TILES, result.getCount());
            converted.set(ModDataComponentTypes.TILE_BLOCK_TILES,
                    result.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT));
            return converted;
        }
        return result;
    }

    public void consumeIngredients(World world) {
        for (int i = 0; i < 16; i++) {
            removeStack(i, 1);
        }
        updateResult(world);
    }

    public Optional<RecipeEntry<TilingTableSmallTileBlockRecipe>> getSmallTileRecipe() {
        if (world == null) return Optional.empty();
        return world.getRecipeManager()
                .getFirstMatch(ModRecipes.TILING_TABLE_TYPE, new TilingTableRecipeInput(getInputInventory()), world);
    }

    public Optional<RecipeEntry<TilingTableTileBlockRecipe>> getTileBlockRecipe() {
        if (world == null) return Optional.empty();
        return world.getRecipeManager()
                .getFirstMatch(ModRecipes.TILING_TABLE_TILE_BLOCK_TYPE, new TilingTableRecipeInput(getInputInventory()), world);
    }

    public Optional<RecipeEntry<TilingTableSmallTileItemRecipe>> getSmallTileItemRecipe() {
        if (world == null) return Optional.empty();
        return world.getRecipeManager()
                .getFirstMatch(ModRecipes.TILING_TABLE_SMALL_TILE_ITEM_TYPE, new TilingTableRecipeInput(getInputInventory()), world);
    }

    public DefaultedList<ItemStack> getInputInventory() {
        DefaultedList<ItemStack> inputInventory = DefaultedList.ofSize(16, ItemStack.EMPTY);
        for (int i = 0; i < 16; i++) {
            inputInventory.set(i, inventory.get(i));
        }
        return inputInventory;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}