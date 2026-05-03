package com.proctoredgames.tiled.block.entity.records;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record SmallTiles(Optional<Item> slot0, Optional<Item> slot1, Optional<Item> slot2, Optional<Item> slot3,
                         Optional<Item> slot4, Optional<Item> slot5, Optional<Item> slot6, Optional<Item> slot7,
                         Optional<Item> slot8, Optional<Item> slot9, Optional<Item> slot10, Optional<Item> slot11,
                         Optional<Item> slot12, Optional<Item> slot13, Optional<Item> slot14, Optional<Item> slot15) {

    public static final SmallTiles DEFAULT = new SmallTiles(
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
    );

    // 1.20.1: sizeLimitedListOf does not exist — use listOf
    // 1.20.1: PacketCodec does not exist (added in 1.20.5) — removed entirely
    public static final Codec<SmallTiles> CODEC = Registries.ITEM.getCodec()
            .listOf()
            .xmap(SmallTiles::new, SmallTiles::stream);

    // Manual network helpers in place of the removed PacketCodec
    public static void encode(PacketByteBuf buf, SmallTiles value) {
        for (Item item : value.stream()) {
            buf.writeVarInt(Registries.ITEM.getRawId(item));
        }
    }

    public static SmallTiles decode(PacketByteBuf buf) {
        List<Item> items = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            items.add(Registries.ITEM.get(buf.readVarInt()));
        }
        return new SmallTiles(items);
    }

    private SmallTiles(List<Item> tiles) {
        this(
                getTile(tiles, 0),  getTile(tiles, 1),  getTile(tiles, 2),  getTile(tiles, 3),
                getTile(tiles, 4),  getTile(tiles, 5),  getTile(tiles, 6),  getTile(tiles, 7),
                getTile(tiles, 8),  getTile(tiles, 9),  getTile(tiles, 10), getTile(tiles, 11),
                getTile(tiles, 12), getTile(tiles, 13), getTile(tiles, 14), getTile(tiles, 15)
        );
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
        }
        Item item = tiles.get(index);
        return item == Items.AIR ? Optional.empty() : Optional.of(item);
    }

    public NbtCompound toNbt(NbtCompound nbt) {
        if (this.equals(DEFAULT)) {
            return nbt;
        }
        // 1.20.1: getOrThrow takes a boolean + consumer, not zero args
        nbt.put("tiles", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, e -> {}));
        return nbt;
    }

    public List<Item> stream() {
        return Stream.of(
                slot0, slot1, slot2,  slot3,
                slot4, slot5, slot6,  slot7,
                slot8, slot9, slot10, slot11,
                slot12, slot13, slot14, slot15
        ).map(item -> item.orElse(Items.AIR)).toList();
    }

    public static SmallTiles fromNbt(@Nullable NbtCompound nbt) {
        return nbt != null && nbt.contains("tiles")
                ? CODEC.parse(NbtOps.INSTANCE, nbt.get("tiles")).result().orElse(DEFAULT)
                : DEFAULT;
    }
}