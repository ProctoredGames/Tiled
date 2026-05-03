package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class SmallTileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";

    private SmallTiles tiles;

    public SmallTileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_TILE_BLOCK_BE, pos, state);
        this.tiles = SmallTiles.DEFAULT;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.tiles.toNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.tiles = SmallTiles.fromNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public SmallTiles getTiles() {
        return this.tiles;
    }

    public void setTiles(SmallTiles tiles) {
        this.tiles = tiles;
        markDirty();
    }

    public void readFrom(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt != null) {
            this.tiles = SmallTiles.fromNbt(nbt);
        } else {
            this.tiles = SmallTiles.DEFAULT;
        }
    }

    public ItemStack asStack() {
        ItemStack stack = ModBlocks.SMALL_TILE_BLOCK.asItem().getDefaultStack();
        if (!this.tiles.equals(SmallTiles.DEFAULT)) {
            NbtCompound tag = new NbtCompound();
            this.tiles.toNbt(tag);
            stack.setSubNbt("BlockEntityTag", tag);
        }
        return stack;
    }

    public static ItemStack getStackWith(SmallTiles tiles) {
        ItemStack stack = ModBlocks.SMALL_TILE_BLOCK.asItem().getDefaultStack();
        if (!tiles.equals(SmallTiles.DEFAULT)) {
            NbtCompound tag = new NbtCompound();
            tiles.toNbt(tag);
            stack.setSubNbt("BlockEntityTag", tag);
        }
        return stack;
    }
}