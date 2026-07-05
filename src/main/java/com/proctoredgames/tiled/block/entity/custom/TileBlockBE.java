package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.EnumMap;
import java.util.Map;

public class TileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";
    public static final String FACE_TILES_NBT_KEY = "face_tiles";

    // Every face defaults to Tiles.DEFAULT; crafted blocks are uniform and
    // faces only diverge through tile trowel edits
    private final Map<Direction, Tiles> faceTiles = new EnumMap<>(Direction.class);

    public TileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILE_BLOCK_BE, pos, state);
    }

    public Tiles getFace(Direction direction) {
        return this.faceTiles.getOrDefault(direction, Tiles.DEFAULT);
    }

    public void setFace(Direction direction, Tiles tiles) {
        this.faceTiles.put(direction, tiles);
        markDirty();
    }

    private void putAllFaces(Tiles tiles) {
        for (Direction direction : Direction.values()) {
            this.faceTiles.put(direction, tiles);
        }
    }

    private boolean isUniform() {
        Tiles first = getFace(Direction.DOWN);
        for (Direction direction : Direction.values()) {
            if (!getFace(direction).equals(first)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeTiles(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        readTiles(nbt);
        if (this.world != null && this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    private void writeTiles(NbtCompound nbt) {
        if (isUniform()) {
            // Keeps the pre-per-face NBT format for uniform blocks
            getFace(Direction.DOWN).toNbt(nbt);
        } else {
            NbtCompound faces = new NbtCompound();
            this.faceTiles.forEach((direction, tiles) ->
                    faces.put(direction.getName(), Tiles.CODEC.encodeStart(NbtOps.INSTANCE, tiles).getOrThrow(false, e -> {})));
            nbt.put(FACE_TILES_NBT_KEY, faces);
        }
    }

    private void readTiles(NbtCompound nbt) {
        this.faceTiles.clear();
        if (nbt.contains(FACE_TILES_NBT_KEY)) {
            NbtCompound faces = nbt.getCompound(FACE_TILES_NBT_KEY);
            for (Direction direction : Direction.values()) {
                if (faces.contains(direction.getName())) {
                    Tiles.CODEC.parse(NbtOps.INSTANCE, faces.get(direction.getName())).result()
                            .ifPresent(tiles -> this.faceTiles.put(direction, tiles));
                }
            }
        } else {
            putAllFaces(Tiles.fromNbt(nbt));
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void readFrom(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        this.faceTiles.clear();
        if (nbt != null) {
            readTiles(nbt);
        }
    }

    public ItemStack asStack() {
        ItemStack stack = ModBlocks.TILE_BLOCK.asItem().getDefaultStack();
        if (!isUniform() || !getFace(Direction.DOWN).equals(Tiles.DEFAULT)) {
            NbtCompound tag = new NbtCompound();
            writeTiles(tag);
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
