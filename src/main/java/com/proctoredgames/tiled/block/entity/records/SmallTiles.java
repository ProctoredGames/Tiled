package com.proctoredgames.tiled.block.entity.records;


import com.mojang.serialization.Codec;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record SmallTiles(Optional<Item> slot0, Optional<Item> slot1, Optional<Item> slot2, Optional<Item> slot3,
                         Optional<Item> slot4, Optional<Item> slot5, Optional<Item> slot6, Optional<Item> slot7,
                         Optional<Item> slot8, Optional<Item> slot9, Optional<Item> slot10, Optional<Item> slot11,
                         Optional<Item> slot12, Optional<Item> slot13, Optional<Item> slot14, Optional<Item> slot15) {
    public static final SmallTiles DEFAULT = new SmallTiles(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<SmallTiles> CODEC = Registries.ITEM.getCodec().sizeLimitedListOf(16).xmap(SmallTiles::new, SmallTiles::stream);
    public static final PacketCodec<RegistryByteBuf, SmallTiles> PACKET_CODEC = PacketCodecs.registryValue(RegistryKeys.ITEM)
            .collect(PacketCodecs.toList(16))
            .xmap(SmallTiles::new, SmallTiles::stream);

    private SmallTiles(List<Item> tiles) {
        this(getTile(tiles, 0), getTile(tiles, 1), getTile(tiles, 2), getTile(tiles, 3),
                getTile(tiles, 4), getTile(tiles, 5), getTile(tiles, 6), getTile(tiles, 7),
                getTile(tiles, 8), getTile(tiles, 9), getTile(tiles, 10), getTile(tiles, 11),
                getTile(tiles, 12), getTile(tiles, 13), getTile(tiles, 14), getTile(tiles, 15));
    }

    public SmallTiles(Item slot0, Item slot1, Item slot2, Item slot3,
                      Item slot4, Item slot5, Item slot6, Item slot7,
                      Item slot8, Item slot9, Item slot10, Item slot11,
                      Item slot12, Item slot13, Item slot14, Item slot15) {
        this(List.of(slot0, slot1, slot2, slot3, slot4, slot5, slot6, slot7,
                slot8, slot9, slot10, slot11, slot12, slot13, slot14, slot15));
    }

    private static Optional<Item> getTile(List<Item> tiles, int index) {
        if (index >= tiles.size()) {
            return Optional.empty();
        } else {
            Item item = (Item)tiles.get(index);
            return Optional.of(item);
        }
    }

    public NbtCompound toNbt(NbtCompound nbt) {
        if (this.equals(DEFAULT)) {
            return nbt;
        } else {
            nbt.put("tiles", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
            return nbt;
        }
    }

    public List<Item> stream() {
        return Stream.of(this.slot0, this.slot1, this.slot2, this.slot3,
                this.slot4, this.slot5, this.slot6, this.slot7,
                this.slot8, this.slot9, this.slot10, this.slot11,
                this.slot12, this.slot13, this.slot14, this.slot15).map(item -> (Item)item.orElse(Items.BRICK)).toList();
    }

    public static SmallTiles fromNbt(@Nullable NbtCompound nbt) {
        return nbt != null && nbt.contains("tiles") ? (SmallTiles)CODEC.parse(NbtOps.INSTANCE, nbt.get("tiles")).result().orElse(DEFAULT) : DEFAULT;
    }
}
