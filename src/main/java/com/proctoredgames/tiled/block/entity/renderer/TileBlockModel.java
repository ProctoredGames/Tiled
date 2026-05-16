package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import com.proctoredgames.tiled.block.entity.records.Tiles;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final int FALLBACK_SPRITE_INDEX = 14; // white

    private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];
    private final Map<Item, Sprite> itemToSprite = new HashMap<>();

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
            Sprite sprite = textureGetter.apply(SPRITE_IDS[i]);
            sprites[i] = sprite;
            itemToSprite.put(SPRITE_ITEMS[i], sprite);
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

        emitTileQuads(context.getEmitter(), tiles);
    }

    @Override
    public void emitItemQuads(
            ItemStack stack,
            Supplier<Random> random,
            RenderContext context
    ) {
        Tiles tiles = stack.getOrDefault(ModDataComponentTypes.TILE_BLOCK_TILES, Tiles.DEFAULT);
        emitTileQuads(context.getEmitter(), tiles);
    }

    private void emitTileQuads(QuadEmitter emitter, Tiles tiles) {
        Sprite tl = spriteFor(tiles.top_left());
        Sprite tr = spriteFor(tiles.top_right());
        Sprite bl = spriteFor(tiles.bottom_left());
        Sprite br = spriteFor(tiles.bottom_right());

        for (Direction dir : Direction.values()) {
            emit(emitter, dir, 0f,  0.5f, 0.5f, 1.0f, tl);
            emit(emitter, dir, 0.5f, 0.5f, 1.0f, 1.0f, tr);
            emit(emitter, dir, 0.0f, 0.0f, 0.5f, 0.5f, bl);
            emit(emitter, dir, 0.5f, 0.0f, 1.0f, 0.5f, br);
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
        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();
    }

    private Sprite spriteFor(Optional<Item> tile) {
        return tile.map(itemToSprite::get).orElse(sprites[FALLBACK_SPRITE_INDEX]);
    }

    //we have no way to get the tile data, so just use white
    @Override public Sprite getParticleSprite() { return sprites[FALLBACK_SPRITE_INDEX]; }

    @Override public List<BakedQuad> getQuads(BlockState s, Direction d, Random r) { return List.of(); }
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isBuiltin() { return false; }
    @Override public boolean hasDepth() { return false; }
    @Override public boolean isSideLit() { return true; }
    @Override public boolean isVanillaAdapter() { return false; }
    @Override public ModelTransformation getTransformation() { return ModelHelper.MODEL_TRANSFORM_BLOCK; }
    @Override public ModelOverrideList getOverrides() { return ModelOverrideList.EMPTY; }
}