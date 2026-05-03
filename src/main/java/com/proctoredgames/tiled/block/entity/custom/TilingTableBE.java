package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.entity.ImplementedInventory;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.recipe.custom.TilingTableSmallTileBlockRecipe;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// 1.20.1: ExtendedScreenHandlerFactory is not generic — it uses PacketByteBuf directly
public class TilingTableBE extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(17, ItemStack.EMPTY);

    public static final int OUTPUT_SLOT = 16;

    protected final PropertyDelegate propertyDelegate;

    public TilingTableBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILING_TABLE_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) { return 0; }
            @Override
            public void set(int index, int value) {}
            @Override
            public int size() { return 17; }
        };
    }

    // 1.20.1: writeScreenOpeningData takes a PacketByteBuf, not a generic type
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
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

    // 1.20.1: no RegistryWrapper parameter
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
    }

    public void updateResult(World world) {
        TilingTableRecipeInput recipeInput = new TilingTableRecipeInput(getInputInventory());
        ItemStack result = ItemStack.EMPTY;

        Optional<RecipeEntry<TilingTableSmallTileBlockRecipe>> smallTileRecipe = getSmallTileRecipe();
        if (smallTileRecipe.isPresent()) {
            result = smallTileRecipe.get().value().craft(recipeInput, world.getRegistryManager());
        } else {
            Optional<RecipeEntry<TilingTableTileBlockRecipe>> tileRecipe = getTileBlockRecipe();
            if (tileRecipe.isPresent()) {
                result = tileRecipe.get().value().craft(recipeInput, world.getRegistryManager());
            }
        }

        inventory.set(OUTPUT_SLOT, result);
        markDirty();
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

    // 1.20.1: no RegistryWrapper parameter
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}