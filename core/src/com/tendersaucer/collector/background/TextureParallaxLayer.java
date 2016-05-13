package com.tendersaucer.collector.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tendersaucer.collector.AssetManager;

/**
 * Parallax layer from single texture
 *
 * Created by Alex on 4/8/2016.
 */
public class TextureParallaxLayer extends ParallaxLayer {

    protected final TextureRegion textureRegion;

    public TextureParallaxLayer(float parallaxRatio, String textureName) {
        super(parallaxRatio);

        textureRegion = AssetManager.getInstance().getTextureRegion(textureName);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, topLeft.x, topLeft.y);
    }

    @Override
    public float getWidth() {
        return textureRegion.getRegionWidth();
    }

    @Override
    public float getHeight() {
        return textureRegion.getRegionHeight();
    }
}
