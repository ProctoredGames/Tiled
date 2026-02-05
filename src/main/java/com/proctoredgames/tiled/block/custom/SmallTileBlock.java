package com.proctoredgames.tiled.block.custom;

import com.mojang.serialization.MapCodec;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
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

public class SmallTileBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<SmallTileBlock> CODEC = SmallTileBlock.createCodec(SmallTileBlock::new);

    public SmallTileBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SmallTileBlockBE(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        SmallTiles tiles = stack.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT);
        if (!tiles.equals(SmallTiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.slot0(), tiles.slot1(), tiles.slot2(), tiles.slot3(),
                            tiles.slot4(), tiles.slot5(), tiles.slot6(), tiles.slot7(),
                            tiles.slot8(), tiles.slot9(), tiles.slot10(), tiles.slot11(),
                            tiles.slot12(), tiles.slot13(), tiles.slot14(), tiles.slot15())
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
        if (world.getBlockEntity(pos) instanceof SmallTileBlockBE be) {
            SmallTiles tiles = stack.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT);
            be.setTiles(tiles);
        }
    }


}
