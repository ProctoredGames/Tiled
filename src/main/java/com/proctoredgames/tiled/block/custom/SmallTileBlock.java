package com.proctoredgames.tiled.block.custom;

import com.mojang.serialization.MapCodec;
import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class SmallTileBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<SmallTileBlock> CODEC = createCodec(SmallTileBlock::new);
    public static final Identifier SMALL_TILE_BLOCK_DYNAMIC_DROP_ID = Identifier.of(Tiled.MOD_ID, "small_tile_block");

    private static final DirectionProperty FACING;

    @Override
    protected MapCodec<SmallTileBlock> getCodec() { return CODEC; }

    public SmallTileBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)(this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
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
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof SmallTileBlockBE blockEntity
                ? blockEntity.asStack()
                : super.getPickStack(world, pos, state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof SmallTileBlockBE smallTileBlockBE) {
            builder.addDynamicDrop(SMALL_TILE_BLOCK_DYNAMIC_DROP_ID, lootConsumer -> {
                lootConsumer.accept(smallTileBlockBE.asStack());
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }
}

