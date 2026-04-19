package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.entity.ImplementedInventory;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.recipe.ModRecipes;
import com.proctoredgames.tiled.recipe.TilingTableRecipe;
import com.proctoredgames.tiled.recipe.TilingTableRecipeInput;
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

    private static final int OUTPUT_SLOT = 16;

    protected final PropertyDelegate propertyDelegate;

    public TilingTableBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILING_TABLE_BE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int size() {
                return 17;
            }
        };
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
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        // Craft one output per tick, not the entire inventory at once
        if (hasRecipe()) {
            craftItem();
            markDirty(world, pos, state);
        }
    }

    private void craftItem() {
        Optional<RecipeEntry<TilingTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        // Use craft() so the output carries the correct SmallTiles data component
        ItemStack output = recipe.get().value().craft(
                new TilingTableRecipeInput(getInputInventory()),
                this.getWorld().getRegistryManager()
        );

        // Consume one item from each of the 16 input slots
        for (int i = 0; i < 16; i++) {
            this.removeStack(i, 1);
        }

        ItemStack currentOutput = this.getStack(OUTPUT_SLOT);
        if (currentOutput.isEmpty()) {
            this.setStack(OUTPUT_SLOT, output.copy());
        } else {
            currentOutput.increment(output.getCount());
        }
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<TilingTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack output = recipe.get().value().craft(
                new TilingTableRecipeInput(getInputInventory()),
                this.getWorld().getRegistryManager()
        );
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private DefaultedList<ItemStack> getInputInventory() {
        DefaultedList<ItemStack> inputInventory = DefaultedList.ofSize(16, ItemStack.EMPTY);
        for (int i = 0; i < 16; i++) {
            inputInventory.set(i, inventory.get(i));
        }
        return inputInventory;
    }

    private Optional<RecipeEntry<TilingTableRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.TILING_TABLE_TYPE, new TilingTableRecipeInput(getInputInventory()), this.getWorld());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        ItemStack current = this.getStack(OUTPUT_SLOT);
        return current.isEmpty() || ItemStack.areItemsEqual(current, output);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        ItemStack current = this.getStack(OUTPUT_SLOT);
        int maxCount = current.isEmpty() ? 64 : current.getMaxCount();
        int currentCount = current.isEmpty() ? 0 : current.getCount();
        return maxCount >= currentCount + count;
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