package com.proctoredgames.tiled.block.custom;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class TileBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final Identifier TILE_BLOCK_DYNAMIC_DROP_ID = new Identifier(Tiled.MOD_ID, "tile_block");

    public TileBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileBlockBE(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient && world.getBlockEntity(pos) instanceof TileBlockBE be) {
            ServerWorld serverWorld = (ServerWorld) world;
            serverWorld.getServer().getPlayerManager().sendToAround(
                null, pos.getX(), pos.getY(), pos.getZ(), 64.0,
                serverWorld.getRegistryKey(), be.toUpdatePacket()
            );
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(TileBlockBE.FACE_TILES_NBT_KEY)) {
            tooltip.add(ScreenTexts.EMPTY);
            tooltip.add(Text.translatable("tooltip.tiled.mixed_faces").formatted(Formatting.GRAY));
            return;
        }

        Tiles tiles = Tiles.DEFAULT;
        if (blockEntityTag != null) {
            tiles = Tiles.fromNbt(blockEntityTag);
        }

        if (!tiles.equals(Tiles.DEFAULT)) {
            tooltip.add(ScreenTexts.EMPTY);
            Stream.of(tiles.top_left(), tiles.top_right(), tiles.bottom_left(), tiles.bottom_right())
                    .forEach(tile -> tooltip.add(new ItemStack((ItemConvertible) tile.orElse(Items.BRICK), 1).getName().copyContentOnly().formatted(Formatting.GRAY)));
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof TileBlockBE blockEntity
                ? blockEntity.asStack()
                : super.getPickStack(world, pos, state);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof TileBlockBE tileBlockBE) {
            builder.addDynamicDrop(TILE_BLOCK_DYNAMIC_DROP_ID, lootConsumer -> {
                lootConsumer.accept(tileBlockBE.asStack());
            });
        }
        return super.getDroppedStacks(state, builder);
    }
}