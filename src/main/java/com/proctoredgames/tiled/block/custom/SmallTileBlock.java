package com.proctoredgames.tiled.block.custom;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class SmallTileBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final Identifier SMALL_TILE_BLOCK_DYNAMIC_DROP_ID = new Identifier(Tiled.MOD_ID, "small_tile_block");

    public SmallTileBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SmallTileBlockBE(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        SmallTiles tiles = SmallTiles.DEFAULT;
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null) {
            tiles = SmallTiles.fromNbt(blockEntityTag);
        }

        if (!tiles.equals(SmallTiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.slot0(), tiles.slot1(), tiles.slot2(), tiles.slot3(),
                            tiles.slot4(), tiles.slot5(), tiles.slot6(), tiles.slot7(),
                            tiles.slot8(), tiles.slot9(), tiles.slot10(), tiles.slot11(),
                            tiles.slot12(), tiles.slot13(), tiles.slot14(), tiles.slot15())
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof SmallTileBlockBE blockEntity
                ? blockEntity.asStack()
                : super.getPickStack(world, pos, state);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof SmallTileBlockBE smallTileBlockBE) {
            builder.addDynamicDrop(SMALL_TILE_BLOCK_DYNAMIC_DROP_ID, lootConsumer -> {
                lootConsumer.accept(smallTileBlockBE.asStack());
            });
        }
        return super.getDroppedStacks(state, builder);
    }
}