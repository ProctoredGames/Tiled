package com.proctoredgames.tiled.item.custom;

import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.SmallTileLayerBE;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.custom.TileLayerBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import com.proctoredgames.tiled.util.TileColors;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileTrowelItem extends Item {

    public TileTrowelItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        World world = context.getWorld();
        BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
        ItemStack offhand = player.getStackInHand(Hand.OFF_HAND);
        Item offhandItem = offhand.getItem();

        if (blockEntity instanceof TileBlockBE tileBlock && TileColors.isTileItem(offhandItem)) {
            Direction face = context.getSide();
            List<Item> tiles = new ArrayList<>(tileBlock.getFace(face).stream());
            int slot = cellIndex(context, face, 2);
            Item newTile = TileColors.concreteForTile(offhandItem);
            if (tiles.get(slot) == newTile) return ActionResult.PASS;
            if (world.isClient()) return ActionResult.SUCCESS;
            Item replaced = tiles.set(slot, newTile);
            tileBlock.setFace(face, new Tiles(tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3)));
            finishSwap(context, player, offhand, TileColors.tileForConcrete(replaced));
            return ActionResult.SUCCESS;
        }

        if (blockEntity instanceof SmallTileBlockBE smallTileBlock && TileColors.isSmallTileItem(offhandItem)) {
            Direction face = context.getSide();
            List<Item> tiles = new ArrayList<>(smallTileBlock.getFace(face).stream());
            int slot = cellIndex(context, face, 4);
            Item newTile = TileColors.concreteForSmallTile(offhandItem);
            if (tiles.get(slot) == newTile) return ActionResult.PASS;
            if (world.isClient()) return ActionResult.SUCCESS;
            Item replaced = tiles.set(slot, newTile);
            smallTileBlock.setFace(face, smallTilesOf(tiles));
            finishSwap(context, player, offhand, TileColors.smallTileForConcrete(replaced));
            return ActionResult.SUCCESS;
        }

        if (blockEntity instanceof TileLayerBE tileLayer && TileColors.isTileItem(offhandItem)) {
            Direction face = targetFace(tileLayer.getFaces(), context.getSide());
            if (face == null) return ActionResult.PASS;
            Tiles pattern = tileLayer.getFace(face);
            if (pattern == null) return ActionResult.PASS;
            List<Item> tiles = new ArrayList<>(pattern.stream());
            int slot = cellIndex(context, face.getOpposite(), 2);
            Item newTile = TileColors.concreteForTile(offhandItem);
            if (tiles.get(slot) == newTile) return ActionResult.PASS;
            if (world.isClient()) return ActionResult.SUCCESS;
            Item replaced = tiles.set(slot, newTile);
            tileLayer.setFace(face, new Tiles(tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3)));
            finishSwap(context, player, offhand, TileColors.tileForConcrete(replaced));
            return ActionResult.SUCCESS;
        }

        if (blockEntity instanceof SmallTileLayerBE smallTileLayer && TileColors.isSmallTileItem(offhandItem)) {
            Direction face = targetFace(smallTileLayer.getFaces(), context.getSide());
            if (face == null) return ActionResult.PASS;
            SmallTiles pattern = smallTileLayer.getFace(face);
            if (pattern == null) return ActionResult.PASS;
            List<Item> tiles = new ArrayList<>(pattern.stream());
            int slot = cellIndex(context, face.getOpposite(), 4);
            Item newTile = TileColors.concreteForSmallTile(offhandItem);
            if (tiles.get(slot) == newTile) return ActionResult.PASS;
            if (world.isClient()) return ActionResult.SUCCESS;
            Item replaced = tiles.set(slot, newTile);
            smallTileLayer.setFace(face, smallTilesOf(tiles));
            finishSwap(context, player, offhand, TileColors.smallTileForConcrete(replaced));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static SmallTiles smallTilesOf(List<Item> tiles) {
        return new SmallTiles(
                tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(3),
                tiles.get(4), tiles.get(5), tiles.get(6), tiles.get(7),
                tiles.get(8), tiles.get(9), tiles.get(10), tiles.get(11),
                tiles.get(12), tiles.get(13), tiles.get(14), tiles.get(15)
        );
    }

    private static void finishSwap(ItemUsageContext context, PlayerEntity player, ItemStack offhand, @Nullable Item refund) {
        if (refund != null) {
            player.getInventory().offerOrDrop(new ItemStack(refund));
        }
        if (!player.getAbilities().creativeMode) {
            offhand.decrement(1);
        }
        Hand hand = context.getHand();
        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(hand));

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            blockEntity.markDirty();
        }
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(pos);
        }
    }

    /**
     * Picks which face of a layer block to edit: the face behind the clicked
     * side, or the only occupied face when an edge of the layer was clicked.
     */
    @Nullable
    private static Direction targetFace(Set<Direction> faces, Direction clickedSide) {
        Direction face = clickedSide.getOpposite();
        if (faces.contains(face)) return face;
        return faces.size() == 1 ? faces.iterator().next() : null;
    }

    /**
     * Maps the clicked position on a face to a tile cell index, matching the
     * quad layout the tile models emit via QuadEmitter.square(). Returns
     * row-major indices counted from the top-left of the face as rendered.
     */
    private static int cellIndex(ItemUsageContext context, Direction visibleFace, int gridSize) {
        BlockPos pos = context.getBlockPos();
        Vec3d hit = context.getHitPos();
        float fx = (float) (hit.x - pos.getX());
        float fy = (float) (hit.y - pos.getY());
        float fz = (float) (hit.z - pos.getZ());

        float a;
        float b;
        switch (visibleFace) {
            case NORTH -> { a = 1 - fx; b = fy; }
            case SOUTH -> { a = fx; b = fy; }
            case WEST -> { a = fz; b = fy; }
            case EAST -> { a = 1 - fz; b = fy; }
            case DOWN -> { a = 1 - fx; b = 1 - fz; }
            default -> { a = 1 - fx; b = fz; }
        }

        int col = MathHelper.clamp((int) (a * gridSize), 0, gridSize - 1);
        int rowFromTop = MathHelper.clamp(gridSize - 1 - (int) (b * gridSize), 0, gridSize - 1);
        return rowFromTop * gridSize + col;
    }
}
