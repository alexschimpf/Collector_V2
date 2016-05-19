package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;

/**
 * Created by Alex on 5/9/2016.
 */
public abstract class RenderedEntity extends Entity implements IRender {

    protected Sprite sprite; // subclasses can add more, if necessary

    public RenderedEntity(EntityDefinition definition) {
        super(definition);

        String textureId = definition.getStringProperty("texture");
        TextureRegion textureRegion =
                AssetManager.getInstance().getTextureRegion(textureId);
        sprite = new Sprite(textureRegion);
        sprite.setSize(getWidth(), getHeight());
    }

    @Override
    protected void tick() {
        super.tick();

        sprite.setPosition(getLeft(), getTop());
        sprite.setOrigin(getWidth() / 2, getHeight() / 2);
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
    }

    @Override
    public void dispose() {
        super.dispose();

        Canvas.getInstance().remove(this);
    }

    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public void addToCanvas() {
        Canvas.getInstance().addToLayer(definition.getLayer(), this);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
