package com.proctoredgames.tiled.block.custom;

import com.mojang.serialization.MapCodec;
import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class TileBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<TileBlock> CODEC = TileBlock.createCodec(TileBlock::new);

    public TileBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileBlockBE(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
        if (!tiles.equals(Tiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.top_left(), tiles.top_right(), tiles.bottom_left(), tiles.bottom_right())
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible)tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }


    @Override
    public void onPlaced(
            World world,
            BlockPos pos,
            BlockState state,
            LivingEntity placer,
            ItemStack stack
    ) {
        if (world.getBlockEntity(pos) instanceof TileBlockBE be) {
            Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
            be.setTiles(tiles);
        }
    }


}
