package com.proctoredgames.tiled.block.custom;

import com.mojang.serialization.MapCodec;
import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.SmallTilesBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class SmallTilesBlock extends MultifaceGrowthBlock implements BlockEntityProvider {

    public static final MapCodec<SmallTilesBlock> CODEC = createCodec(SmallTilesBlock::new);
    public static final Identifier SMALL_TILES_DYNAMIC_DROP_ID = Identifier.of(Tiled.MOD_ID, "small_tiles");

    private final LichenGrower grower = new LichenGrower(this);

    public SmallTilesBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<SmallTilesBlock> getCodec() {
        return CODEC;
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SmallTilesBE(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof SmallTilesBE blockEntity) {
            SmallTiles tiles = itemStack.getOrDefault(ModDataComponentTypes.SMALL_TILE_BLOCK_TILES, SmallTiles.DEFAULT);
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
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState newState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (newState != state && world.getBlockEntity(pos) instanceof SmallTilesBE blockEntity) {
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
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof SmallTilesBE smallTilesBE) {
            builder.addDynamicDrop(SMALL_TILES_DYNAMIC_DROP_ID, lootConsumer -> {
                for (Direction direction : DIRECTIONS) {
                    if (smallTilesBE.hasFace(direction)) {
                        lootConsumer.accept(smallTilesBE.asStackForFace(direction));
                    }
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof SmallTilesBE blockEntity) {
            for (Direction direction : DIRECTIONS) {
                if (blockEntity.hasFace(direction)) {
                    return blockEntity.asStackForFace(direction);
                }
            }
        }
        return super.getPickStack(world, pos, state);
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
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }
}
