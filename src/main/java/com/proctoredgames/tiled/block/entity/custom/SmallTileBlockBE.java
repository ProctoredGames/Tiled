package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.ModBlocks;
import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.SmallTilesPerFace;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.EnumMap;
import java.util.Map;

public class SmallTileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";
    public static final String FACE_TILES_NBT_KEY = "face_tiles";

    // Every face defaults to SmallTiles.DEFAULT; crafted blocks are uniform
    // and faces only diverge through tile trowel edits
    private final Map<Direction, SmallTiles> faceTiles = new EnumMap<>(Direction.class);

    public SmallTileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_TILE_BLOCK_BE, pos, state);
    }

    public SmallTiles getFace(Direction direction) {
        return this.faceTiles.getOrDefault(direction, SmallTiles.DEFAULT);
    }

    public void setFace(Direction direction, SmallTiles tiles) {
        this.faceTiles.put(direction, tiles);
        this.markDirty();
    }

    private void putAllFaces(SmallTiles tiles) {
        for (Direction direction : Direction.values()) {
            this.faceTiles.put(direction, tiles);
        }
    }

    private boolean isUniform() {
        SmallTiles first = getFace(Direction.DOWN);
        for (Direction direction : Direction.values()) {
            if (!getFace(direction).equals(first)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (isUniform()) {
            // Keeps the pre-per-face NBT format for uniform blocks
            getFace(Direction.DOWN).toNbt(nbt);
        } else {
            NbtCompound faces = new NbtCompound();
            this.faceTiles.forEach((direction, tiles) ->
                    faces.put(direction.getName(), SmallTiles.CODEC.encodeStart(NbtOps.INSTANCE, tiles).getOrThrow()));
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
                    SmallTiles.CODEC.parse(NbtOps.INSTANCE, faces.get(direction.getName())).result()
                            .ifPresent(tiles -> this.faceTiles.put(direction, tiles));
                }
            }
        } else {
            putAllFaces(SmallTiles.fromNbt(nbt));
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
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if (isUniform()) {
            componentMapBuilder.add(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, getFace(Direction.DOWN));
        } else {
            componentMapBuilder.add(ModDataComponentTypes.SMALL_TILE_BLOCK_FACE_TILES,
                    new SmallTilesPerFace(Map.copyOf(this.faceTiles)));
        }
    }

    @Override
    protected void readComponents(BlockEntity.ComponentsAccess components) {
        super.readComponents(components);
        this.faceTiles.clear();
        SmallTilesPerFace perFace = components.get(ModDataComponentTypes.SMALL_TILE_BLOCK_FACE_TILES);
        if (perFace != null) {
            this.faceTiles.putAll(perFace.faces());
        } else {
            putAllFaces(components.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT));
        }
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove(TILES_NBT_KEY);
        nbt.remove(FACE_TILES_NBT_KEY);
    }

}
