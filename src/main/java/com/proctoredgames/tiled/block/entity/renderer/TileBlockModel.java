package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.Tiles;
import com.proctoredgames.tiled.block.entity.custom.TileBlockBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
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
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/black_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/brown_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/cyan_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/green_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/light_blue_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/light_gray_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/lime_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/magenta_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/orange_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/pink_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/purple_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/red_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/white_tiles")),
            new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier.of(Tiled.MOD_ID, "block/yellow_tiles")),
    };

    private final Sprite[] sprites = new Sprite[SPRITE_IDS.length];

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(); // No other models
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        // Not used
    }

    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        // Load sprites once
        for (int i = 0; i < SPRITE_IDS.length; i++) {
            sprites[i] = textureGetter.apply(SPRITE_IDS[i]);
        }
        return this;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos,
                               Supplier<Random> randomSupplier, RenderContext context) {
        TileBlockBE be = (TileBlockBE) world.getBlockEntity(pos);
        Tiles tiles = be != null ? be.getTiles() : Tiles.DEFAULT;

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        Sprite topLeft = sprites[getTextureIdFromTile(tiles.top_left())];
        Sprite topRight = sprites[getTextureIdFromTile(tiles.top_right())];
        Sprite bottomLeft = sprites[getTextureIdFromTile(tiles.bottom_left())];
        Sprite bottomRight = sprites[getTextureIdFromTile(tiles.bottom_right())];

        for (Direction dir : Direction.values()) {
            emitter.square(dir, 0f, 0f, 0.5f, 0.5f, 0f);
            emitter.spriteBake(topLeft, QuadEmitter.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();

            emitter.square(dir, 0.5f, 0f, 1f, 0.5f, 0f);
            emitter.spriteBake(topRight, QuadEmitter.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();

            emitter.square(dir, 0f, 0.5f, 0.5f, 1f, 0f);
            emitter.spriteBake(bottomLeft, QuadEmitter.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();

            emitter.square(dir, 0.5f, 0.5f, 1f, 1f, 0f);
            emitter.spriteBake(bottomRight, QuadEmitter.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }

        // Output dynamically generated mesh
        builder.build().outputTo(context.getEmitter());
    }

    private int getTextureIdFromTile(Optional<Item> tile) {
        if (tile == null || tile.isEmpty()) return 0;

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
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return List.of(); // Not used, FabricBakedModel handles dynamic quads
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return sprites[0]; // fallback black
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
        mesh.outputTo(renderContext.getEmitter());
    }
}
