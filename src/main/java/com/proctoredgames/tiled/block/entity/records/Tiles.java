package com.proctoredgames.tiled.block.entity.records;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record Tiles(Optional<Item> top_left, Optional<Item> top_right, Optional<Item> bottom_left, Optional<Item> bottom_right) {

    public static final Tiles DEFAULT = new Tiles(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

    public static final Codec<Tiles> CODEC = Registries.ITEM.getCodec()
            .sizeLimitedListOf(4)
            .xmap(Tiles::new, Tiles::stream);

    // 1.20.1: no RegistryByteBuf or PacketCodecs.registryValue — encode/decode manually
    public static final PacketCodec<PacketByteBuf, Tiles> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public Tiles decode(PacketByteBuf buf) {
            List<Item> items = new ArrayList<>(4);
            for (int i = 0; i < 4; i++) {
                int rawId = buf.readVarInt();
                items.add(Registries.ITEM.get(rawId));
            }
            return new Tiles(items);
        }

        @Override
        public void encode(PacketByteBuf buf, Tiles value) {
            for (Item item : value.stream()) {
                buf.writeVarInt(Registries.ITEM.getRawId(item));
            }
        }
    };

    private Tiles(List<Item> tiles) {
        this(getTile(tiles, 0), getTile(tiles, 1), getTile(tiles, 2), getTile(tiles, 3));
    }

    public Tiles(Item top_left, Item top_right, Item bottom_left, Item bottom_right) {
        this(List.of(top_left, top_right, bottom_left, bottom_right));
    }

    private static Optional<Item> getTile(List<Item> tiles, int index) {
        if (index >= tiles.size()) {
            return Optional.empty();
        }
        Item item = tiles.get(index);
        return Optional.of(item);
    }

    public NbtCompound toNbt(NbtCompound nbt) {
        if (this.equals(DEFAULT)) {
            return nbt;
        }
        nbt.put("tiles", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
        return nbt;
    }

    public List<Item> stream() {
        return Stream.of(
                this.top_left, this.top_right,
                this.bottom_left, this.bottom_right
        ).map(item -> item.orElse(Items.AIR)).toList();
    }

    public static Tiles fromNbt(@Nullable NbtCompound nbt) {
        return nbt != null && nbt.contains("tiles")
                ? CODEC.parse(NbtOps.INSTANCE, nbt.get("tiles")).result().orElse(DEFAULT)
                : DEFAULT;
    }
}