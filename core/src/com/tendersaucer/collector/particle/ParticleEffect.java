package com.tendersaucer.collector.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * System of particles
 *
 * Created by Alex on 4/8/2016.
 */
public class ParticleEffect implements IUpdate, IRender {

    protected static final Pool<Particle> PARTICLE_POOL = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    protected boolean keepProportions;
    protected long lastLoopTime;
    protected Float loopDelay;
    protected Color startColor;
    protected Color endColor;

    protected final Vector2 position;
    protected final Vector2 sizeRange;
    protected final Vector2 durationRange;
    protected final Vector2 particlesRange;
    protected final Vector2 minVelocity, maxVelocity;
    protected final Vector2 scale;
    protected final Vector2 scaleCenter;
    protected final Vector2 alphaInterpolation;
    protected final Vector2 angularVelocityRange;
    protected final Vector2 minPositionOffsets, maxPositionOffsets;
    protected final Array<String> textureKeys;

    protected final Array<Particle> particles;

    public ParticleEffect() {
        particles = new Array<Particle>();

        textureKeys = new Array<String>();

        keepProportions = false;
        lastLoopTime = 0;

        position = new Vector2(0, 0);
        sizeRange = new Vector2(0, 0);
        durationRange = new Vector2(0, 0);
        particlesRange = new Vector2(0, 0);
        minVelocity = new Vector2(0, 0);
        maxVelocity = new Vector2(0, 0);
        scale = new Vector2(1, 1);
        scaleCenter = new Vector2(0, 0);
        alphaInterpolation = new Vector2(1, 1);
        angularVelocityRange = new Vector2(0, 0);
        minPositionOffsets = new Vector2(0, 0);
        maxPositionOffsets = new Vector2(0, 0);
    }

    public ParticleEffect(ParticleEffect other) {
        textureKeys = other.textureKeys;
        keepProportions = other.keepPropertions();
        durationRange = other.getDurationRange();
        particlesRange = other.getParticlesRange();
        minVelocity = other.getMinVelocity();
        maxVelocity = other.getMaxVelocity();
        scale = other.getScale();
        scaleCenter = other.getScaleCenter();
        alphaInterpolation = other.getAlphaInterpolation();
        angularVelocityRange = other.getAngularVelocityRange();
        minPositionOffsets = other.getMinPositionOffsets();
        maxPositionOffsets = other.getMaxPositionOffsets();

        lastLoopTime = 0;
        position = new Vector2(0, 0);
        sizeRange = new Vector2(0, 0);
        particles = new Array<Particle>();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        return false;
    }

    public void setKeepPropertions(boolean keepProportions) {
        this.keepProportions = keepProportions;
    }

    public void setLoopDelay(Float loopDelay) {
        this.loopDelay = loopDelay;
    }

    public void setStartColor(float r, float g, float b) {
        if (startColor == null) {
            startColor = new Color();
        }

        startColor.set(r, g, b, 1);
    }

    public void setEndColor(float r, float g, float b) {
        if (endColor == null) {
            endColor = new Color();
        }

        endColor.set(r, g, b, 1);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setMinVelocity(float x, float y) {
        minVelocity.set(x, y);
    }

    public void setMaxVelocity(float x, float y) {
        maxVelocity.set(x, y);
    }

    public void setScale(float x, float y) {
        scale.set(x, y);
    }

    public void setScaleCenter(float x, float y) {
        scaleCenter.set(x, y);
    }

    public void setMinPositionOffsets(float x, float y) {
        minPositionOffsets.set(x, y);
    }

    public void setMaxPositionOffsets(float x, float y) {
        maxPositionOffsets.set(x, y);
    }

    public void setSizeRange(float min, float max) {
        sizeRange.set(min, max);
    }

    public void setDurationRange(float min, float max) {
        durationRange.set(min, max);
    }

    public void setParticlesRange(float min, float max) {
        particlesRange.set(min, max);
    }

    public void setAngularVelocityRange(float min, float max) {
        angularVelocityRange.set(min, max);
    }

    public void setAlphaInterpolation(float start, float end) {
        alphaInterpolation.set(start, end);
    }

    public Array<String> getTextureKeys() {
        return textureKeys;
    }

    public boolean keepPropertions() {
        return keepProportions;
    }

    public boolean loops() {
        return getLoopDelay() != null;
    }

    public long getLastLoopTime() {
        return lastLoopTime;
    }

    public Float getLoopDelay() {
        return loopDelay;
    }

    public Color getStartColor() {
        return startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSizeRange() {
        return sizeRange;
    }

    public Vector2 getDurationRange() {
        return durationRange;
    }

    public Vector2 getParticlesRange() {
        return particlesRange;
    }

    public Vector2 getMinVelocity() {
        return minVelocity;
    }

    public Vector2 getMaxVelocity() {
        return maxVelocity;
    }

    public Vector2 getScale() {
        return scale;
    }

    public Vector2 getScaleCenter() {
        return scaleCenter;
    }

    public Vector2 getAlphaInterpolation() {
        return alphaInterpolation;
    }

    public Vector2 getAngularVelocityRange() {
        return angularVelocityRange;
    }

    public Vector2 getMinPositionOffsets() {
        return minPositionOffsets;
    }

    public Vector2 getMaxPositionOffsets() {
        return maxPositionOffsets;
    }
}
