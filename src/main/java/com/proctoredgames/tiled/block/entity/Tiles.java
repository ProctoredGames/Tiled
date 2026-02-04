package com.proctoredgames.tiled.block.entity;


import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

public record Tiles(Optional<Item> top_left, Optional<Item> top_right, Optional<Item> bottom_left, Optional<Item> bottom_right) {
    public static final Tiles DEFAULT = new Tiles(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<Tiles> CODEC = Registries.ITEM.getCodec().sizeLimitedListOf(4).xmap(Tiles::new, Tiles::stream);
    public static final PacketCodec<RegistryByteBuf, Tiles> PACKET_CODEC = PacketCodecs.registryValue(RegistryKeys.ITEM)
            .collect(PacketCodecs.toList(4))
            .xmap(Tiles::new, Tiles::stream);

    private Tiles(List<Item> Tiles) {
        this(getTile(Tiles, 0), getTile(Tiles, 1), getTile(Tiles, 2), getTile(Tiles, 3));
    }

    public Tiles(Item back, Item left, Item right, Item front) {
        this(List.of(back, left, right, front));
    }

    private static Optional<Item> getTile(List<Item> Tiles, int index) {
        if (index >= Tiles.size()) {
            return Optional.empty();
        } else {
            Item item = (Item)Tiles.get(index);
            return item == Items.BRICK ? Optional.empty() : Optional.of(item);
        }
    }

    public NbtCompound toNbt(NbtCompound nbt) {
        if (this.equals(DEFAULT)) {
            return nbt;
        } else {
            nbt.put("Tiles", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
            return nbt;
        }
    }

    public List<Item> stream() {
        return Stream.of(this.top_left, this.top_right, this.bottom_left, this.bottom_right).map(item -> (Item)item.orElse(Items.BRICK)).toList();
    }

    public static Tiles fromNbt(@Nullable NbtCompound nbt) {
        return nbt != null && nbt.contains("Tiles") ? (Tiles)CODEC.parse(NbtOps.INSTANCE, nbt.get("Tiles")).result().orElse(DEFAULT) : DEFAULT;
    }
}
