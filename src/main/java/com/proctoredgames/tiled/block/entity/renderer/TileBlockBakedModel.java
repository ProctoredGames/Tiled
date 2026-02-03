package com.proctoredgames.tiled.block.entity.renderer;

import com.proctoredgames.tiled.Tiled;
import com.proctoredgames.tiled.block.entity.custom.TileBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TileBlockBakedModel implements BakedModel, FabricBakedModel {
    private final Map<Integer, Mesh> meshCache = new HashMap<>();
    private final Sprite[] colorSprites = new Sprite[16];
    private final Sprite particleSprite;
    private final ModelTransformation transformation;
    private final ModelOverrideList overrides;

    public TileBlockBakedModel(){
        for(int i = 0; i<16; i++){
            String colorName = getColorName(i);
            Identifier textureId = Identifier.of(Tiled.MOD_ID,"block/"+colorName+"_tiles");

            SpriteAtlasTexture blockAtlas = MinecraftClient.getInstance()
                    .getSpriteAtlasManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
            colorSprites[i] = blockAtlas.getSprite(textureId);
        }
        particleSprite = colorSprites[0];
        transformation = ModelTransformation.NONE;
        overrides = ModelOverrideList.EMPTY;
    }

    private String getColorName(int index){
        String[] colorNames = {
                "black","blue","brown","cyan",
                "gray", "green", "light_blue", "light_gray",
                "lime", "magenta", "orange", "pink",
                "purple", "red", "white", "yellow"
        };
        return colorNames[index];
    }

    private int getCacheKey(byte[] colors) {
        return ((colors[0] & 0xFF)<<24) |
                ((colors[1] & 0xFF)<<16) |
                ((colors[2] & 0xFF)<<8) |
                (colors[3] & 0xff);
    }

    private Mesh getOrCreateMesh(byte[] colors){
        int key = getCacheKey(colors);
        return meshCache.computeIfAbsent(key, k -> createMesh(colors));
    }

    private Mesh createMesh(byte[] colors){
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if(renderer==null) return null;

        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        for(Direction direction : Direction.values()){
            addFaceQuads(emitter, direction, colors);
        }

        return builder.build();
    }

    private void addFaceQuads(QuadEmitter emitter, Direction face, byte[] colors){
        float[][] quadrantUVs = {
                {0.0f, 0.5f, 0.5f, 1.0f},
                {0.5f, 0.5f, 1.0f, 1.0f},
                {0.0f, 0.0f, 0.5f, 0.5f},
                {0.5f, 0.0f, 1.0f, 0.5f}
        };

        for(int quadrant = 0; quadrant < 4; quadrant ++){
            Sprite sprite = colorSprites[colors[quadrant] & 0xFF];
            float[] uv = quadrantUVs[quadrant];

            emitQuadrant(emitter, face, sprite, uv[0], uv[1], uv[2], uv[3]);
        }
    }

    private void emitQuadrant(QuadEmitter emitter, Direction face,
                              Sprite sprite, float u0, float v0, float u1, float v1){
        emitter.square(face, 0,0,1,1,0);

        for(int i = 0; i<4; i++){
            float u, v;
            switch(i){
                case 0: u = u0; v = v1; break;
                case 1: u = u1; v = v1; break;
                case 2: u = u1; v = v0; break;
                case 3: u = u0; v = v0; break;
                default: u = 0; v = 0;
            }

            emitter.sprite(i, 0, sprite.getFrameU(u*16), sprite.getFrameV(v*16));
        }

        emitter.color(-1,-1,-1,-1);
        emitter.emit();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if(blockView.getBlockEntity(pos) instanceof TileBlockEntity blockEntity){
            byte [] colors = blockEntity.getColors();
            Mesh mesh = getOrCreateMesh(colors);
            if(mesh != null) {
                context.meshConsumer().accept(mesh);
            }
        } else {
            Mesh mesh = getOrCreateMesh(new byte[]{0,0,0,0});
            if (mesh != null) {
                context.meshConsumer().accept(mesh);
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return particleSprite;
    }

    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return overrides;
    }
}
