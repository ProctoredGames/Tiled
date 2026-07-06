package com.proctoredgames.tiled.block.custom;

import com.mojang.serialization.MapCodec;
import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileLayerBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class TileLayerBlock extends MultifaceGrowthBlock implements BlockEntityProvider, Waterloggable {

    public static final MapCodec<TileLayerBlock> CODEC = createCodec(TileLayerBlock::new);
    public static final Identifier TILE_LAYER_DYNAMIC_DROP_ID = Identifier.of(Tiled.MOD_ID, "tile_layer");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final LichenGrower grower = new LichenGrower(this);

    public TileLayerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected MapCodec<TileLayerBlock> getCodec() {
        return CODEC;
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem()) && super.canReplace(state, context);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileLayerBE(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof TileLayerBE blockEntity) {
            Tiles tiles = itemStack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
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
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        BlockState newState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (newState != state && world.getBlockEntity(pos) instanceof TileLayerBE blockEntity) {
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
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof TileLayerBE tileLayerBE) {
            builder.addDynamicDrop(TILE_LAYER_DYNAMIC_DROP_ID, lootConsumer -> {
                for (Direction direction : DIRECTIONS) {
                    if (tileLayerBE.hasFace(direction)) {
                        lootConsumer.accept(tileLayerBE.asStackForFace(direction));
                    }
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof TileLayerBE blockEntity) {
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
        Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
        if (!tiles.equals(Tiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.top_left(), tiles.top_right(), tiles.bottom_left(), tiles.bottom_right())
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }
}
