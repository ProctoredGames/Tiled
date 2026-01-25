package com.proctoredgames.tiled.block.entity.custom;

import com.proctoredgames.tiled.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class SmallTileBlockEntity extends BlockEntity {
    public SmallTileBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMALL_TILE_BLOCK_BE, pos, state);
    }
}
