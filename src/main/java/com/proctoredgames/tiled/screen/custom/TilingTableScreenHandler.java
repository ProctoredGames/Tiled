package com.proctoredgames.tiled.screen.custom;

import com.proctoredgames.tiled.block.entity.custom.TilingTableBE;
import com.proctoredgames.tiled.screen.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TilingTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final TilingTableBE blockEntity;

    // 1.20.1: ExtendedScreenHandlerType passes a PacketByteBuf, not a BlockPos.
    // This constructor is called by the client-side factory registered in ModScreenHandlers.
    // The buf contains the BlockPos written by TilingTableBE.writeScreenOpeningData().
    public TilingTableScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(17));
    }

    // Server-side constructor — called directly by TilingTableBE.createMenu()
    public TilingTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                    BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.TILING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((TilingTableBE) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        // Output slot — preview only, player cannot insert, consuming inputs on take
        this.addSlot(new Slot(inventory, TilingTableBE.OUTPUT_SLOT, 133, 26) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                World world = player.getWorld();
                if (!world.isClient()) {
                    TilingTableScreenHandler.this.blockEntity.consumeIngredients(world);
                }
                super.onTakeItem(player, stack);
            }
        });

        // Input slots — each one triggers a result update when changed
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new Slot(inventory, j + i * 4, 21 + j * 18, -1 + i * 18) {
                    @Override
                    public void markDirty() {
                        super.markDirty();
                        World world = TilingTableScreenHandler.this.blockEntity.getWorld();
                        if (world != null && !world.isClient()) {
                            TilingTableScreenHandler.this.blockEntity.updateResult(world);
                        }
                    }
                });
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(arrayPropertyDelegate);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}