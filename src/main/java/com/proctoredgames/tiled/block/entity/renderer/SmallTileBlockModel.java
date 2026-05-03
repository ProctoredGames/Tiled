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
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
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
import java.util.List;
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

    private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];

    // 1.20.1: Identifier.of() -> new Identifier()
    private static Identifier id(String path) {
        return new Identifier(Tiled.MOD_ID, "block/" + path);
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> loader) {}

    // 1.20.1: Baker -> ModelLoader in bake() signature
    @Override
    public BakedModel bake(
            ModelLoader loader,
            Function<SpriteIdentifier, Sprite> textureGetter,
            ModelBakeSettings settings,
            Identifier id
    ) {
        for (int i = 0; i < SPRITE_IDS.length; i++) {
            sprites[i] = textureGetter.apply(SPRITE_IDS[i]);
        }
        return this;
    }

    @Override
    public void emitBlockQuads(
            BlockRenderView world,
            BlockState state,
            BlockPos pos,
            Supplier<Random> random,
            RenderContext context
    ) {
        SmallTiles tiles = SmallTiles.DEFAULT;

        if (world.getBlockEntity(pos) instanceof SmallTileBlockBE be) {
            tiles = be.getTiles();
        }

        QuadEmitter emitter = context.getEmitter();

        Sprite s0  = sprites[getTextureIdFromTile(tiles.slot0())];
        Sprite s1  = sprites[getTextureIdFromTile(tiles.slot1())];
        Sprite s2  = sprites[getTextureIdFromTile(tiles.slot2())];
        Sprite s3  = sprites[getTextureIdFromTile(tiles.slot3())];
        Sprite s4  = sprites[getTextureIdFromTile(tiles.slot4())];
        Sprite s5  = sprites[getTextureIdFromTile(tiles.slot5())];
        Sprite s6  = sprites[getTextureIdFromTile(tiles.slot6())];
        Sprite s7  = sprites[getTextureIdFromTile(tiles.slot7())];
        Sprite s8  = sprites[getTextureIdFromTile(tiles.slot8())];
        Sprite s9  = sprites[getTextureIdFromTile(tiles.slot9())];
        Sprite s10 = sprites[getTextureIdFromTile(tiles.slot10())];
        Sprite s11 = sprites[getTextureIdFromTile(tiles.slot11())];
        Sprite s12 = sprites[getTextureIdFromTile(tiles.slot12())];
        Sprite s13 = sprites[getTextureIdFromTile(tiles.slot13())];
        Sprite s14 = sprites[getTextureIdFromTile(tiles.slot14())];
        Sprite s15 = sprites[getTextureIdFromTile(tiles.slot15())];

        for (Direction dir : Direction.values()) {
            emit(emitter, dir, 0f,    0.75f, 0.25f, 1.0f,  s0);
            emit(emitter, dir, 0.25f, 0.75f, 0.5f,  1.0f,  s1);
            emit(emitter, dir, 0.5f,  0.75f, 0.75f, 1.0f,  s2);
            emit(emitter, dir, 0.75f, 0.75f, 1.0f,  1.0f,  s3);
            emit(emitter, dir, 0f,    0.5f,  0.25f, 0.75f, s4);
            emit(emitter, dir, 0.25f, 0.5f,  0.5f,  0.75f, s5);
            emit(emitter, dir, 0.5f,  0.5f,  0.75f, 0.75f, s6);
            emit(emitter, dir, 0.75f, 0.5f,  1.0f,  0.75f, s7);
            emit(emitter, dir, 0f,    0.25f, 0.25f, 0.5f,  s8);
            emit(emitter, dir, 0.25f, 0.25f, 0.5f,  0.5f,  s9);
            emit(emitter, dir, 0.5f,  0.25f, 0.75f, 0.5f,  s10);
            emit(emitter, dir, 0.75f, 0.25f, 1.0f,  0.5f,  s11);
            emit(emitter, dir, 0f,    0.0f,  0.25f, 0.25f, s12);
            emit(emitter, dir, 0.25f, 0.0f,  0.5f,  0.25f, s13);
            emit(emitter, dir, 0.5f,  0.0f,  0.75f, 0.25f, s14);
            emit(emitter, dir, 0.75f, 0.0f,  1.0f,  0.25f, s15);
        }
    }

    @Override
    public void emitItemQuads(
            ItemStack stack,
            Supplier<Random> random,
            RenderContext context
    ) {
        // 1.20.1: no data components — read from BlockEntityTag NBT
        SmallTiles tiles = SmallTiles.DEFAULT;
        NbtCompound blockEntityTag = stack.getSubNbt("BlockEntityTag");
        if (blockEntityTag != null) {
            tiles = SmallTiles.fromNbt(blockEntityTag);
        }

        QuadEmitter emitter = context.getEmitter();

        Sprite s0  = sprites[getTextureIdFromTile(tiles.slot0())];
        Sprite s1  = sprites[getTextureIdFromTile(tiles.slot1())];
        Sprite s2  = sprites[getTextureIdFromTile(tiles.slot2())];
        Sprite s3  = sprites[getTextureIdFromTile(tiles.slot3())];
        Sprite s4  = sprites[getTextureIdFromTile(tiles.slot4())];
        Sprite s5  = sprites[getTextureIdFromTile(tiles.slot5())];
        Sprite s6  = sprites[getTextureIdFromTile(tiles.slot6())];
        Sprite s7  = sprites[getTextureIdFromTile(tiles.slot7())];
        Sprite s8  = sprites[getTextureIdFromTile(tiles.slot8())];
        Sprite s9  = sprites[getTextureIdFromTile(tiles.slot9())];
        Sprite s10 = sprites[getTextureIdFromTile(tiles.slot10())];
        Sprite s11 = sprites[getTextureIdFromTile(tiles.slot11())];
        Sprite s12 = sprites[getTextureIdFromTile(tiles.slot12())];
        Sprite s13 = sprites[getTextureIdFromTile(tiles.slot13())];
        Sprite s14 = sprites[getTextureIdFromTile(tiles.slot14())];
        Sprite s15 = sprites[getTextureIdFromTile(tiles.slot15())];

        for (Direction dir : Direction.values()) {
            emit(emitter, dir, 0f,    0.75f, 0.25f, 1.0f,  s0);
            emit(emitter, dir, 0.25f, 0.75f, 0.5f,  1.0f,  s1);
            emit(emitter, dir, 0.5f,  0.75f, 0.75f, 1.0f,  s2);
            emit(emitter, dir, 0.75f, 0.75f, 1.0f,  1.0f,  s3);
            emit(emitter, dir, 0f,    0.5f,  0.25f, 0.75f, s4);
            emit(emitter, dir, 0.25f, 0.5f,  0.5f,  0.75f, s5);
            emit(emitter, dir, 0.5f,  0.5f,  0.75f, 0.75f, s6);
            emit(emitter, dir, 0.75f, 0.5f,  1.0f,  0.75f, s7);
            emit(emitter, dir, 0f,    0.25f, 0.25f, 0.5f,  s8);
            emit(emitter, dir, 0.25f, 0.25f, 0.5f,  0.5f,  s9);
            emit(emitter, dir, 0.5f,  0.25f, 0.75f, 0.5f,  s10);
            emit(emitter, dir, 0.75f, 0.25f, 1.0f,  0.5f,  s11);
            emit(emitter, dir, 0f,    0.0f,  0.25f, 0.25f, s12);
            emit(emitter, dir, 0.25f, 0.0f,  0.5f,  0.25f, s13);
            emit(emitter, dir, 0.5f,  0.0f,  0.75f, 0.25f, s14);
            emit(emitter, dir, 0.75f, 0.0f,  1.0f,  0.25f, s15);
        }
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

    private int getTextureIdFromTile(Optional<Item> tile) {
        if (tile.isEmpty()) return 0;
        Item item = tile.get();
        if (item == Items.BLACK_CONCRETE)      return 0;
        if (item == Items.BLUE_CONCRETE)       return 1;
        if (item == Items.BROWN_CONCRETE)      return 2;
        if (item == Items.CYAN_CONCRETE)       return 3;
        if (item == Items.GRAY_CONCRETE)       return 4;
        if (item == Items.GREEN_CONCRETE)      return 5;
        if (item == Items.LIGHT_BLUE_CONCRETE) return 6;
        if (item == Items.LIGHT_GRAY_CONCRETE) return 7;
        if (item == Items.LIME_CONCRETE)       return 8;
        if (item == Items.MAGENTA_CONCRETE)    return 9;
        if (item == Items.ORANGE_CONCRETE)     return 10;
        if (item == Items.PINK_CONCRETE)       return 11;
        if (item == Items.PURPLE_CONCRETE)     return 12;
        if (item == Items.RED_CONCRETE)        return 13;
        if (item == Items.WHITE_CONCRETE)      return 14;
        if (item == Items.YELLOW_CONCRETE)     return 15;
        return 0;
    }

    @Override public Sprite getParticleSprite() { return sprites[14]; }
    @Override public List<BakedQuad> getQuads(BlockState s, Direction d, Random r) { return List.of(); }
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isBuiltin() { return false; }
    @Override public boolean hasDepth() { return false; }
    @Override public boolean isSideLit() { return true; }
    @Override public boolean isVanillaAdapter() { return false; }
    @Override public ModelTransformation getTransformation() { return ModelHelper.MODEL_TRANSFORM_BLOCK; }
    @Override public ModelOverrideList getOverrides() { return ModelOverrideList.EMPTY; }
}