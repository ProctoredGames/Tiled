package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class TileLayerBE extends BlockEntity {

    public static final String FACE_TILES_NBT_KEY = "face_tiles";

    private final Map<Direction, Tiles> faceTiles = new EnumMap<>(Direction.class);

    public TileLayerBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILE_LAYER_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.faceTiles.isEmpty()) {
            NbtCompound faces = new NbtCompound();
            this.faceTiles.forEach((direction, tiles) ->
                    faces.put(direction.getName(), Tiles.CODEC.encodeStart(NbtOps.INSTANCE, tiles).getOrThrow()));
            nbt.put(FACE_TILES_NBT_KEY, faces);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.faceTiles.clear();
        if (nbt.contains(FACE_TILES_NBT_KEY)) {
            NbtCompound faces = nbt.getCompound(FACE_TILES_NBT_KEY);
            for (Direction direction : Direction.values()) {
                if (faces.contains(direction.getName())) {
                    Tiles.CODEC.parse(NbtOps.INSTANCE, faces.get(direction.getName())).result()
                            .ifPresent(tiles -> this.faceTiles.put(direction, tiles));
                }
            }
        }
        if (this.world != null && this.world.isClient()) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createComponentlessNbt(registryLookup);
    }

    public boolean hasFace(Direction direction) {
        return this.faceTiles.containsKey(direction);
    }

    @Nullable
    public Tiles getFace(Direction direction) {
        return this.faceTiles.get(direction);
    }

    public Set<Direction> getFaces() {
        return this.faceTiles.keySet();
    }

    public void setFace(Direction direction, Tiles tiles) {
        this.faceTiles.put(direction, tiles);
        this.markDirty();
    }

    public void removeFace(Direction direction) {
        this.faceTiles.remove(direction);
        this.markDirty();
    }

    public ItemStack asStackForFace(Direction direction) {
        return getStackWith(this.faceTiles.getOrDefault(direction, Tiles.DEFAULT));
    }

    public static ItemStack getStackWith(Tiles tiles) {
        ItemStack itemStack = ModBlocks.TILE_LAYER.asItem().getDefaultStack();
        itemStack.set(ModDataComponentTypes.TILE_BLOCK_TILES, tiles);
        return itemStack;
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove(FACE_TILES_NBT_KEY);
    }
}
