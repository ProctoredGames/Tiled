package com.proctoredgames.tiled.block.entity.renderer;

import com.mojang.datafixers.util.Pair;
import com.proctoredgames.tiled.Tiled;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class TileBlockModelPlugin implements ModelLoadingPlugin {

    @Override
    public void onInitializeModelLoader(Context context) {
        context.addModels(
                Identifier.of(Tiled.MOD_ID, "block/white_tiles")
        );

        context.resolveModel().register(context1 -> {
            Identifier id = context1.id();
            if(id.equals(Identifier.of(Tiled.MOD_ID, "block/tile_block"))){
                return new UnbakedModel() {
                    @Override
                    public BakedModel bake(Baker baker,
                                           Function<SpriteIdentifier, Sprite> textureGetter,
                                           ModelBakeSettings rotationContainer) {
                        return new TileBlockBakedModel();
                    }
                    @Override
                    public Collection<SpriteIdentifier> getTextureDependencies(
                            Function<Identifier, UnbakedModel> unbakedModelGetter,
                            Set<Pair<String, String>>unresolvedTextureReferences){
                        return Collections.emptyList();
                    }

                };
            }
            return null;
        });
    }
}
