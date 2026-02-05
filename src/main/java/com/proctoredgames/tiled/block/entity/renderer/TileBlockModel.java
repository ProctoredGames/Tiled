package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.component.ModDataComponentTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
public class TileBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

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

    private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];

    private static Identifier id(String path) {
        return Identifier.of(Tiled.MOD_ID, "block/" + path);
    }

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
            ModelBakeSettings settings
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
        Tiles tiles = Tiles.DEFAULT;

        if (world.getBlockEntity(pos) instanceof TileBlockBE be) {
            tiles = be.getTiles();
        }

        QuadEmitter emitter = context.getEmitter();

        Sprite tl = sprites[getTextureIdFromTile(tiles.top_left())];
        Sprite tr = sprites[getTextureIdFromTile(tiles.top_right())];
        Sprite bl = sprites[getTextureIdFromTile(tiles.bottom_left())];
        Sprite br = sprites[getTextureIdFromTile(tiles.bottom_right())];

        for (Direction dir : Direction.values()) {
            emit(emitter, dir, 0f,   0.5f,   0.5f, 1.0f, tl);
            emit(emitter, dir, 0.5f, 0.5f,   1.0f,   1.0f, tr);
            emit(emitter, dir, 0.0f,   0.0f, 0.5f, 0.5f,   bl);
            emit(emitter, dir, 0.5f, 0.0f, 1.0f,   0.5f,   br);
        }

    }

    private static void emit(
            QuadEmitter emitter,
            Direction dir,
            float x1, float y1, float x2, float y2,
            Sprite sprite
    ) {
        emitter.square(dir, x1, y1, x2, y2, 0f);
        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();
    }

    private int getTextureIdFromTile(Optional<Item> tile) {
        if (tile.isEmpty()) return 1;

        Item item = tile.get();
        if (item == Items.BLACK_CONCRETE) return 0;
        if (item == Items.BLUE_CONCRETE) return 1;
        if (item == Items.BROWN_CONCRETE) return 2;
        if (item == Items.CYAN_CONCRETE) return 3;
        if (item == Items.GRAY_CONCRETE) return 4;
        if (item == Items.GREEN_CONCRETE) return 5;
        if (item == Items.LIGHT_BLUE_CONCRETE) return 6;
        if (item == Items.LIGHT_GRAY_CONCRETE) return 7;
        if (item == Items.LIME_CONCRETE) return 8;
        if (item == Items.MAGENTA_CONCRETE) return 9;
        if (item == Items.ORANGE_CONCRETE) return 10;
        if (item == Items.PINK_CONCRETE) return 11;
        if (item == Items.PURPLE_CONCRETE) return 12;
        if (item == Items.RED_CONCRETE) return 13;
        if (item == Items.WHITE_CONCRETE) return 14;
        if (item == Items.YELLOW_CONCRETE) return 15;
        return 0; // fallback
    }

    @Override
    public void emitItemQuads(
            ItemStack stack,
            Supplier<Random> random,
            RenderContext context
    ) {
        Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);

        QuadEmitter emitter = context.getEmitter();

        Sprite tl = sprites[getTextureIdFromTile(tiles.top_left())];
        Sprite tr = sprites[getTextureIdFromTile(tiles.top_right())];
        Sprite bl = sprites[getTextureIdFromTile(tiles.bottom_left())];
        Sprite br = sprites[getTextureIdFromTile(tiles.bottom_right())];

        for (Direction dir : Direction.values()) {
            emit(emitter, dir, 0f,   0.5f,   0.5f, 1.0f, tl);
            emit(emitter, dir, 0.5f, 0.5f,   1.0f,   1.0f, tr);
            emit(emitter, dir, 0.0f,   0.0f, 0.5f, 0.5f,   bl);
            emit(emitter, dir, 0.5f, 0.0f, 1.0f,   0.5f,   br);
        }
    }

    @Override public Sprite getParticleSprite() {
        //we have no way to get the tile data, so just use white (the inside of the tile block)
        return sprites[14];
    }

    @Override public List<BakedQuad> getQuads(BlockState s, Direction d, Random r) { return List.of(); }
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isBuiltin() { return false; }
    @Override public boolean hasDepth() { return false; }
    @Override public boolean isSideLit() { return true; }
    @Override public boolean isVanillaAdapter() { return false; }
    @Override public ModelTransformation getTransformation() { return ModelHelper.MODEL_TRANSFORM_BLOCK; }
    @Override public ModelOverrideList getOverrides() { return ModelOverrideList.EMPTY; }
}
