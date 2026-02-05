package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class SmallTileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";

    private SmallTiles tiles;

    public SmallTileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_TILE_BLOCK_BE, pos, state);
        this.tiles = SmallTiles.DEFAULT;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        this.tiles.toNbt(nbt);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.tiles = SmallTiles.fromNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createComponentlessNbt(registryLookup);
    }

    public SmallTiles getTiles() {
        return this.tiles;
    }

    public void setTiles(SmallTiles tiles) {
        if (tiles == null) {
            tiles = SmallTiles.DEFAULT;
        }

        this.tiles = tiles;

        // Mark for saving
        markDirty();

        // Sync + rerender on client
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }


    public void readFrom(ItemStack stack) {
        this.readComponents(stack);
    }

    public ItemStack asStack() {
        ItemStack itemStack = ModBlocks.SMALL_TILE_BLOCK.asItem().getDefaultStack();
        itemStack.applyComponentsFrom(this.createComponentMap());
        return itemStack;
    }

    public static ItemStack getStackWith(SmallTiles tiles) {
        ItemStack itemStack = ModBlocks.SMALL_TILE_BLOCK.asItem().getDefaultStack();
        itemStack.set(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, tiles);
        return itemStack;
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove("tiles");
    }
}

