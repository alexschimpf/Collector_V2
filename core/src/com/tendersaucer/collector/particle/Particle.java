package com.tendersaucer.collector.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Created by Alex on 4/30/2016.
 */
public class Particle implements IUpdate, IRender, Pool.Poolable {

    protected float duration;
    protected long startTime;
    protected float angularVelocity;
    protected Sprite sprite;
    protected final Vector2 velocity;

    public Particle() {
        angularVelocity = 0;
        duration = 0;
        velocity = new Vector2(0, 0);
        sprite = new Sprite();
    }

    @Override
    public void reset() {
        angularVelocity = 0;
        duration = 0;
        velocity.set(0, 0);
        sprite = new Sprite();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    @Override
    public boolean update() {
        float delta = Gdx.graphics.getDeltaTime();
        float dx = velocity.x * delta;
        float dy = velocity.y * delta;
        sprite.setPosition(sprite.getX() + dx, sprite.getY() + dy);
        sprite.rotate(angularVelocity * delta);

        return getAge() > duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setStarted() {
        startTime = TimeUtils.millis();
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setSprite(Sprite sprite) {
        this.sprite.set(sprite);
        resetOrigin();
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
        resetOrigin();
    }

    public void setScale(float scaleX, float scaleY) {
        sprite.setScale(scaleX, scaleY);
        resetOrigin();
    }

    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public void setAlpha(float alpha) {
        sprite.setAlpha(alpha);
    }

    public void setOrigin(float x, float y) {
        sprite.setOrigin(x, y);
    }

    public void setColor(Color color) {
        sprite.setColor(color);
    }

    public void setColor(float r, float b, float g) {
        sprite.setColor(r, g, b, getAlpha());
    }

    public void setColor(float r, float b, float g, float a) {
        sprite.setColor(r, b, g, a);
    }

    public float getAge() {
        return TimeUtils.timeSinceMillis(startTime);
    }

    public float getAgeToLifeRatio() {
        return Math.min(1, getAge() / duration);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public float getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAlpha() {
        return sprite.getColor().a;
    }

    public float getScaleX() {
        return sprite.getScaleX();
    }

    public float getScaleY() {
        return sprite.getScaleY();
    }

    public float getOriginX() {
        return sprite.getOriginX();
    }

    public float getOriginY() {
        return sprite.getOriginX();
    }

    public Color getColor() {
        return sprite.getColor();
    }

    private void resetOrigin() {
        setOrigin(sprite.getWidth() / 2 , sprite.getHeight() / 2);
    }
}
