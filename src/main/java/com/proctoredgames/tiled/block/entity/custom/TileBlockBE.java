package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class TileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";

    private Tiles tiles;

    public TileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILE_BLOCK_BE, pos, state);
        this.tiles = Tiles.DEFAULT;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.tiles.toNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.tiles = Tiles.fromNbt(nbt);
        if (this.world != null && this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public Tiles getTiles() {
        return this.tiles;
    }

    public void setTiles(Tiles tiles) {
        this.tiles = tiles;
        markDirty();
    }

    public void readFrom(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt != null) {
            this.tiles = Tiles.fromNbt(nbt);
        } else {
            this.tiles = Tiles.DEFAULT;
        }
    }

    public ItemStack asStack() {
        ItemStack stack = ModBlocks.TILE_BLOCK.asItem().getDefaultStack();
        if (!this.tiles.equals(Tiles.DEFAULT)) {
            NbtCompound tag = new NbtCompound();
            this.tiles.toNbt(tag);
            stack.setSubNbt("BlockEntityTag", tag);
        }
        return stack;
    }

    public static ItemStack getStackWith(Tiles tiles) {
        ItemStack stack = ModBlocks.TILE_BLOCK.asItem().getDefaultStack();
        if (!tiles.equals(Tiles.DEFAULT)) {
            NbtCompound tag = new NbtCompound();
            tiles.toNbt(tag);
            stack.setSubNbt("BlockEntityTag", tag);
        }
        return stack;
    }
}