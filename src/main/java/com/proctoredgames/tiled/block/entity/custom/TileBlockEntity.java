package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TileBlockEntity extends BlockEntity {

    private byte[] colors = new byte[4];

    public TileBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILE_BLOCK_BE, pos, state);
        colors[0] = 0;
        colors[1] = 0;
        colors[2] = 0;
        colors[3] = 0;
    }

    public void setColors(byte[] colors) {
        if (colors.length == 4) {
            System.arraycopy(colors, 0, this.colors, 0, 4);
            markDirty();
            sync();
        }
    }

    public byte[] getColors() {
        return colors.clone();
    }

    private void sync() {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("Colors")) {
            byte[] loaded = nbt.getByteArray("Colors");
            System.arraycopy(loaded, 0, colors, 0, Math.min(loaded.length, 4));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putByteArray("Colors", colors);
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
