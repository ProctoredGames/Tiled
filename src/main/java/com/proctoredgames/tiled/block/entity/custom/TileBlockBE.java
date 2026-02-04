package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TileBlockBE extends BlockEntity {

    public static final String TILES_NBT_KEY = "tiles";

    private Tiles tiles;

    public TileBlockBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TILE_BLOCK_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        this.tiles.toNbt(nbt);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.tiles = Tiles.fromNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createComponentlessNbt(registryLookup);
    }

    public Direction getHorizontalFacing() {
        return this.getCachedState().get(Properties.HORIZONTAL_FACING);
    }

    public Tiles getTiles() {
        return this.tiles;
    }

    public void readFrom(ItemStack stack) {
        this.readComponents(stack);
    }

    public ItemStack asStack() {
        ItemStack itemStack = Items.DECORATED_POT.getDefaultStack();
        itemStack.applyComponentsFrom(this.createComponentMap());
        return itemStack;
    }

    public static ItemStack getStackWith(Tiles tiles) {
        ItemStack itemStack = Items.DECORATED_POT.getDefaultStack();
        itemStack.set(ModDataComponentTypes.TILE_BLOCK_TILES, tiles);
        return itemStack;
    }

//    @Nullable
//    @Override
//    public RegistryKey<LootTable> getLootTable() {
//        return this.lootTableId;
//    }
//
//    @Override
//    public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {
//        this.lootTableId = lootTable;
//    }
//
//    @Override
//    public long getLootTableSeed() {
//        return this.lootTableSeed;
//    }
//
//    @Override
//    public void setLootTableSeed(long lootTableSeed) {
//        this.lootTableSeed = lootTableSeed;
//    }

//    @Override
//    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
//        super.addComponents(componentMapBuilder);
//        componentMapBuilder.add(DataComponentTypes.POT_DECORATIONS, this.sherds);
//        componentMapBuilder.add(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of(this.stack)));
//    }

//    @Override
//    protected void readComponents(BlockEntity.ComponentsAccess components) {
//        super.readComponents(components);
//        this.tiles = components.getOrDefault(DataComponentTypes.POT_DECORATIONS, Tiles.DEFAULT);
//    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove("tiles");
    }
}

