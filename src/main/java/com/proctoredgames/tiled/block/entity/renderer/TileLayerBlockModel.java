package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileLayerBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class TileLayerBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

    // Distance of the visible layer surface from the supporting face (0.2/16)
    private static final float LAYER_DEPTH = 1f - 0.0125f;
    // Depth of the flat item quads: a 1px slab centered like vanilla flat items
    private static final float FLAT_ITEM_DEPTH = 7.5f / 16f;
    // Center of the sprite's edge pixel row/column, sampled by the item side strips
    private static final float EDGE_PIXEL = 0.5f / 16f;

    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("black_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("brown_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("cyan_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("green_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("light_blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("light_gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("lime_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("magenta_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("orange_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("pink_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("purple_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("red_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("white_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("yellow_tiles"))
    };

    private static final Item[] SPRITE_ITEMS = new Item[]{
            Items.BLACK_CONCRETE,
            Items.BLUE_CONCRETE,
            Items.BROWN_CONCRETE,
            Items.CYAN_CONCRETE,
            Items.GRAY_CONCRETE,
            Items.GREEN_CONCRETE,
            Items.LIGHT_BLUE_CONCRETE,
            Items.LIGHT_GRAY_CONCRETE,
            Items.LIME_CONCRETE,
            Items.MAGENTA_CONCRETE,
            Items.ORANGE_CONCRETE,
            Items.PINK_CONCRETE,
            Items.PURPLE_CONCRETE,
            Items.RED_CONCRETE,
            Items.WHITE_CONCRETE,
            Items.YELLOW_CONCRETE
    };

    private static final SpriteIdentifier WHITE_CONCRETE_SPRITE_ID = new SpriteIdentifier(
            PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
            new Identifier("minecraft", "block/white_concrete")
    );

    private static final int FALLBACK_SPRITE_INDEX = 14; // white tiles

    private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];
    private final Map<Item, Sprite> itemToSprite = new HashMap<>();
    private Sprite whiteConcreteSprite;

    private static Identifier id(String path) {
        return new Identifier(Tiled.MOD_ID, "block/" + path);
    }

    // --- UnbakedModel ---

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> loader) {}

    @Override
    public BakedModel bake(
            Baker baker,
            Function<SpriteIdentifier, Sprite> textureGetter,
            ModelBakeSettings settings,
            Identifier id
    ) {
        for (int i = 0; i < SPRITE_IDS.length; i++) {
            Sprite sprite = textureGetter.apply(SPRITE_IDS[i]);
            sprites[i] = sprite;
            itemToSprite.put(SPRITE_ITEMS[i], sprite);
        }
        whiteConcreteSprite = textureGetter.apply(WHITE_CONCRETE_SPRITE_ID);
        return this;
    }

    // --- FabricBakedModel ---

    @Override
    public void emitBlockQuads(
            BlockRenderView world,
            BlockState state,
            BlockPos pos,
            Supplier<Random> random,
            RenderContext context
    ) {
        if (!(world.getBlockEntity(pos) instanceof TileLayerBE be)) return;

        QuadEmitter emitter = context.getEmitter();
        for (Direction attachment : Direction.values()) {
            Tiles tiles = be.getFace(attachment);
            if (tiles != null) {
                emitFaceQuads(emitter, attachment.getOpposite(), tiles, LAYER_DEPTH, false);
            }
        }
    }

    @Override
    public void emitItemQuads(
            ItemStack stack,
            Supplier<Random> random,
            RenderContext context
    ) {
        Tiles tiles = Tiles.DEFAULT;
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null) {
            tiles = Tiles.fromNbt(blockEntityTag);
        }

        QuadEmitter emitter = context.getEmitter();
        // 1px prism like vanilla flat items: pattern front and back, edge strips around;
        // the back is h-flipped so its edges line up with the front and side strips
        emitFaceQuads(emitter, Direction.SOUTH, tiles, FLAT_ITEM_DEPTH, false);
        emitFaceQuads(emitter, Direction.NORTH, tiles, FLAT_ITEM_DEPTH, true);
        emitEdge(emitter, Direction.UP, 0f, 0.5f, spriteFor(tiles.top_left()));
        emitEdge(emitter, Direction.UP, 0.5f, 1f, spriteFor(tiles.top_right()));
        emitEdge(emitter, Direction.DOWN, 0f, 0.5f, spriteFor(tiles.bottom_left()));
        emitEdge(emitter, Direction.DOWN, 0.5f, 1f, spriteFor(tiles.bottom_right()));
        emitEdge(emitter, Direction.WEST, 0f, 0.5f, spriteFor(tiles.bottom_left()));
        emitEdge(emitter, Direction.WEST, 0.5f, 1f, spriteFor(tiles.top_left()));
        emitEdge(emitter, Direction.EAST, 0f, 0.5f, spriteFor(tiles.bottom_right()));
        emitEdge(emitter, Direction.EAST, 0.5f, 1f, spriteFor(tiles.top_right()));
    }

    private void emitFaceQuads(QuadEmitter emitter, Direction dir, Tiles tiles, float depth, boolean flipU) {
        emit(emitter, dir, 0f, 0.5f, 0.5f, 1.0f, spriteFor(tiles.top_left()), depth, flipU);
        emit(emitter, dir, 0.5f, 0.5f, 1.0f, 1.0f, spriteFor(tiles.top_right()), depth, flipU);
        emit(emitter, dir, 0.0f, 0.0f, 0.5f, 0.5f, spriteFor(tiles.bottom_left()), depth, flipU);
        emit(emitter, dir, 0.5f, 0.0f, 1.0f, 0.5f, spriteFor(tiles.bottom_right()), depth, flipU);
    }

    private static void emit(
            QuadEmitter emitter,
            Direction dir,
            float x1, float y1, float x2, float y2,
            Sprite sprite,
            float depth,
            boolean flipU
    ) {
        if (dir == Direction.UP || dir == Direction.DOWN) {
            x1 = 1 - x1;
            x2 = 1 - x2;
            y1 = 1 - y1;
            y2 = 1 - y2;
        }
        emitter.square(dir, x1, y1, x2, y2, depth);
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV | (flipU ? MutableQuadView.BAKE_FLIP_U : 0));
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();
    }

    // One side strip of the 1px item slab, spanning a1..a2 along the edge and
    // sampling the sprite's edge pixels like vanilla item extrusion
    private static void emitEdge(QuadEmitter emitter, Direction side, float a1, float a2, Sprite sprite) {
        float near = FLAT_ITEM_DEPTH;
        float far = 1f - FLAT_ITEM_DEPTH;
        if (side.getAxis() == Direction.Axis.Y) {
            emitter.square(side, a1, near, a2, far, 0f);
        } else {
            emitter.square(side, near, a1, far, a2, 0f);
        }
        for (int i = 0; i < 4; i++) {
            float u;
            float v;
            switch (side) {
                case UP -> { u = emitter.x(i); v = EDGE_PIXEL; }
                case DOWN -> { u = emitter.x(i); v = 1f - EDGE_PIXEL; }
                case WEST -> { u = EDGE_PIXEL; v = 1f - emitter.y(i); }
                default -> { u = 1f - EDGE_PIXEL; v = 1f - emitter.y(i); }
            }
            emitter.sprite(i, 0, u, v);
        }
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_NORMALIZED);
        emitter.spriteColor(0, -1, -1, -1, -1);
        emitter.emit();
    }

    private Sprite spriteFor(Optional<Item> tile) {
        return tile.map(itemToSprite::get).orElse(sprites[FALLBACK_SPRITE_INDEX]);
    }

    @Override public Sprite getParticleSprite() { return whiteConcreteSprite; }
    @Override public List<BakedQuad> getQuads(BlockState s, Direction d, Random r) { return List.of(); }
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isBuiltin() { return false; }
    @Override public boolean hasDepth() { return false; }
    @Override public boolean isSideLit() { return false; }
    @Override public boolean isVanillaAdapter() { return false; }
    @Override public ModelTransformation getTransformation() { return FlatItemTransforms.FLAT_ITEM; }
    @Override public ModelOverrideList getOverrides() { return ModelOverrideList.EMPTY; }
}
