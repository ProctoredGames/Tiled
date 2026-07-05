package com.proctoredgames.tiled.block.custom;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TilesBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class TilesBlock extends MultifaceGrowthBlock implements BlockEntityProvider {

    public static final Identifier TILES_DYNAMIC_DROP_ID = new Identifier(Tiled.MOD_ID, "tiles");

    private final LichenGrower grower = new LichenGrower(this);

    public TilesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TilesBE(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof TilesBE blockEntity) {
            Tiles tiles = Tiles.fromNbt(itemStack.getSubNbt("BlockEntityTag"));
            for (Direction direction : DIRECTIONS) {
                if (hasDirection(state, direction) && !blockEntity.hasFace(direction)) {
                    blockEntity.setFace(direction, tiles);
                }
            }
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(pos);
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState newState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (newState != state && world.getBlockEntity(pos) instanceof TilesBE blockEntity) {
            boolean removed = !newState.isOf(this);
            for (Direction face : DIRECTIONS) {
                if (hasDirection(state, face) && (removed || !hasDirection(newState, face))) {
                    ItemStack drop = blockEntity.asStackForFace(face);
                    blockEntity.removeFace(face);
                    if (world instanceof ServerWorld serverWorld) {
                        Block.dropStack(serverWorld, pos, drop);
                    }
                }
            }
        }
        return newState;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof TilesBE tilesBE) {
            builder.addDynamicDrop(TILES_DYNAMIC_DROP_ID, lootConsumer -> {
                for (Direction direction : DIRECTIONS) {
                    if (tilesBE.hasFace(direction)) {
                        lootConsumer.accept(tilesBE.asStackForFace(direction));
                    }
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof TilesBE blockEntity) {
            for (Direction direction : DIRECTIONS) {
                if (blockEntity.hasFace(direction)) {
                    return blockEntity.asStackForFace(direction);
                }
            }
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        Tiles tiles = Tiles.DEFAULT;
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null) {
            tiles = Tiles.fromNbt(blockEntityTag);
        }

        if (!tiles.equals(Tiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.top_left(), tiles.top_right(), tiles.bottom_left(), tiles.bottom_right())
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }
}
