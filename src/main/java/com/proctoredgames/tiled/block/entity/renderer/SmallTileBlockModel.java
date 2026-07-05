package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.SmallTileBlockBE;
import com.proctoredgames.tiled.block.entity.records.SmallTiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
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
public class SmallTileBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

    private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_black_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_brown_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_cyan_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_green_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_light_blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_light_gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_lime_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_magenta_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_orange_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_pink_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_purple_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_red_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_white_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("small_yellow_tiles"))
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
        QuadEmitter emitter = context.getEmitter();
        if (world.getBlockEntity(pos) instanceof SmallTileBlockBE be) {
            for (Direction dir : Direction.values()) {
                emitFaceQuads(emitter, dir, be.getFace(dir));
            }
        } else {
            emitTileQuads(emitter, SmallTiles.DEFAULT);
        }
    }

    @Override
    public void emitItemQuads(
            ItemStack stack,
            Supplier<Random> random,
            RenderContext context
    ) {
        QuadEmitter emitter = context.getEmitter();
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(SmallTileBlockBE.FACE_TILES_NBT_KEY)) {
            NbtCompound faces = blockEntityTag.getCompound(SmallTileBlockBE.FACE_TILES_NBT_KEY);
            for (Direction dir : Direction.values()) {
                SmallTiles tiles = faces.contains(dir.getName())
                        ? SmallTiles.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, faces.get(dir.getName())).result().orElse(SmallTiles.DEFAULT)
                        : SmallTiles.DEFAULT;
                emitFaceQuads(emitter, dir, tiles);
            }
            return;
        }

        SmallTiles tiles = SmallTiles.DEFAULT;
        if (blockEntityTag != null) {
            tiles = SmallTiles.fromNbt(blockEntityTag);
        }
        emitTileQuads(emitter, tiles);
    }

    private void emitTileQuads(QuadEmitter emitter, SmallTiles tiles) {
        for (Direction dir : Direction.values()) {
            emitFaceQuads(emitter, dir, tiles);
        }
    }

    private void emitFaceQuads(QuadEmitter emitter, Direction dir, SmallTiles tiles) {
        emit(emitter, dir, 0f,    0.75f, 0.25f, 1.0f,  spriteFor(tiles.slot0()));
        emit(emitter, dir, 0.25f, 0.75f, 0.5f,  1.0f,  spriteFor(tiles.slot1()));
        emit(emitter, dir, 0.5f,  0.75f, 0.75f, 1.0f,  spriteFor(tiles.slot2()));
        emit(emitter, dir, 0.75f, 0.75f, 1.0f,  1.0f,  spriteFor(tiles.slot3()));
        emit(emitter, dir, 0f,    0.5f,  0.25f, 0.75f, spriteFor(tiles.slot4()));
        emit(emitter, dir, 0.25f, 0.5f,  0.5f,  0.75f, spriteFor(tiles.slot5()));
        emit(emitter, dir, 0.5f,  0.5f,  0.75f, 0.75f, spriteFor(tiles.slot6()));
        emit(emitter, dir, 0.75f, 0.5f,  1.0f,  0.75f, spriteFor(tiles.slot7()));
        emit(emitter, dir, 0f,    0.25f, 0.25f, 0.5f,  spriteFor(tiles.slot8()));
        emit(emitter, dir, 0.25f, 0.25f, 0.5f,  0.5f,  spriteFor(tiles.slot9()));
        emit(emitter, dir, 0.5f,  0.25f, 0.75f, 0.5f,  spriteFor(tiles.slot10()));
        emit(emitter, dir, 0.75f, 0.25f, 1.0f,  0.5f,  spriteFor(tiles.slot11()));
        emit(emitter, dir, 0f,    0.0f,  0.25f, 0.25f, spriteFor(tiles.slot12()));
        emit(emitter, dir, 0.25f, 0.0f,  0.5f,  0.25f, spriteFor(tiles.slot13()));
        emit(emitter, dir, 0.5f,  0.0f,  0.75f, 0.25f, spriteFor(tiles.slot14()));
        emit(emitter, dir, 0.75f, 0.0f,  1.0f,  0.25f, spriteFor(tiles.slot15()));
    }

    private static void emit(
            QuadEmitter emitter,
            Direction dir,
            float x1, float y1, float x2, float y2,
            Sprite sprite
    ) {
        if (dir == Direction.UP || dir == Direction.DOWN) {
            x1 = 1 - x1;
            x2 = 1 - x2;
            y1 = 1 - y1;
            y2 = 1 - y2;
        }
        emitter.square(dir, x1, y1, x2, y2, 0f);
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
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
    @Override public boolean isSideLit() { return true; }
    @Override public boolean isVanillaAdapter() { return false; }
    @Override public ModelTransformation getTransformation() { return ModelHelper.MODEL_TRANSFORM_BLOCK; }
    @Override public ModelOverrideList getOverrides() { return ModelOverrideList.EMPTY; }
}