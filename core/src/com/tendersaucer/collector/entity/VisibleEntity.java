package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.screen.Canvas;

/**
 * Created by Alex on 5/9/2016.
 */
public abstract class VisibleEntity extends Entity {

    protected Sprite sprite; // subclasses can add more, if necessary

    public VisibleEntity(EntityDefinition definition) {
        super(definition);

        String textureName = definition.getStringProperty("texture");
        TextureRegion textureRegion =
                AssetManager.getInstance().getTextureRegion(textureName);
        sprite = new Sprite(textureRegion);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    @Override
    public void dispose() {
        super.dispose();

        Canvas.getInstance().remove(this);
    }

    @Override
    protected void tick() {
        super.tick();

        sprite.setPosition(getLeft(), getTop());
        sprite.setOrigin(getWidth() / 2, getHeight() / 2);
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
    }

    public Sprite getSprite() {
        return sprite;
    }
}
