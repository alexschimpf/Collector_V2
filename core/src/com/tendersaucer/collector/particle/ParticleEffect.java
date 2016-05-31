package com.tendersaucer.collector.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.particle.modifiers.ParticleModifier;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.ConversionUtils;
import com.tendersaucer.collector.util.RandomUtils;
import com.tendersaucer.collector.util.Vector2Pool;

import java.util.Iterator;

/**
 * Created by Alex on 4/30/2016.
 */
public class ParticleEffect implements IUpdate, IRender, Disposable {

    protected static final Pool<Particle> particlePool = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    protected final Vector2 position;
    protected final Vector2 sizeRange;
    protected final Array<Sprite> sprites;
    protected final Array<Particle> particles;
    protected final Array<ParticleModifier> modifiers;
    protected final Vector2 durationRange;
    protected final Vector2 particlesRange;
    protected final Vector2 xOffsetRange;
    protected final Vector2 yOffsetRange;
    protected final Vector2 vxRange;
    protected final Vector2 vyRange;
    protected final Vector2 velocitySplits;
    protected final Vector2 angularVelocityRange;
    protected final Vector2 redRange;
    protected final Vector2 blueRange;
    protected final Vector2 greenRange;
    protected final Vector2 alphaRange;
    protected long lastLoopTime;
    protected Float loopDelay; // loops indefinitely if not null

    public ParticleEffect(JsonValue json) {
        lastLoopTime = 0;

        Vector2Pool vector2Pool = Vector2Pool.getInstance();
        position = vector2Pool.obtain(0, 0);
        sizeRange = vector2Pool.obtain(0, 0);
        durationRange = vector2Pool.obtain(0, 0);
        particlesRange = vector2Pool.obtain(0, 0);
        xOffsetRange = vector2Pool.obtain(0, 0);
        yOffsetRange = vector2Pool.obtain(0, 0);
        vxRange = vector2Pool.obtain(0, 0);
        vyRange = vector2Pool.obtain(0, 0);
        velocitySplits = vector2Pool.obtain(0, 0);
        angularVelocityRange = vector2Pool.obtain(0, 0);
        redRange = vector2Pool.obtain(0, 0);
        blueRange = vector2Pool.obtain(0, 0);
        greenRange = vector2Pool.obtain(0, 0);
        alphaRange = vector2Pool.obtain(0, 0);

        modifiers = new Array<ParticleModifier>();
        particles = new Array<Particle>();
        sprites = new Array<Sprite>();

        load(json);
    }

    @Override
    public void dispose() {
        Canvas.getInstance().remove(this);
        for (Particle particle : particles) {
            particlePool.free(particle);
        }

        Vector2Pool vector2Pool = Vector2Pool.getInstance();
        vector2Pool.free(position);
        vector2Pool.free(sizeRange);
        vector2Pool.free(durationRange);
        vector2Pool.free(particlesRange);
        vector2Pool.free(xOffsetRange);
        vector2Pool.free(yOffsetRange);
        vector2Pool.free(vxRange);
        vector2Pool.free(vyRange);
        vector2Pool.free(velocitySplits);
        vector2Pool.free(angularVelocityRange);
        vector2Pool.free(redRange);
        vector2Pool.free(blueRange);
        vector2Pool.free(greenRange);
        vector2Pool.free(alphaRange);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Particle particle : particles) {
            particle.render(spriteBatch);
        }
    }

    @Override
    public boolean update() {
        Iterator<Particle> particlesIter = particles.iterator();
        while (particlesIter.hasNext()) {
            Particle particle = particlesIter.next();

            if (particle.update()) {
                particlesIter.remove();
                particlePool.free(particle);
            } else {
                for (ParticleModifier modifier : modifiers) {
                    if (modifier == null) {
                        continue;
                    }

                    modifier.modify(particle);
                }
            }
        }

        if (loops() && TimeUtils.timeSinceMillis(lastLoopTime) > loopDelay) {
            lastLoopTime = TimeUtils.millis();
            createParticles();
        }

        return !loops() && particles.size <= 0;
    }

    public void begin(Vector2 position, Vector2 sizeRange, int layer) {
        this.position.set(position);
        this.sizeRange.set(sizeRange);

        createParticles();

        if (loops()) {
            lastLoopTime = TimeUtils.millis();
        }

        Canvas.getInstance().addToLayer(layer, this);
    }

    public boolean loops() {
        return loopDelay != null;
    }

    public void setDurationRange(float min, float max) {
        durationRange.set(min, max);
    }

    public void setParticlesRange(float min, float max) {
        particlesRange.set(min, max);
    }

    public void setXOffsetRange(float min, float max) {
        xOffsetRange.set(min, max);
    }

    public void setYOffsetRange(float min, float max) {
        yOffsetRange.set(min, max);
    }

    public void setVXRange(float min, float max) {
        vxRange.set(min, max);
    }

    public void setVYRange(float min, float max) {
        vyRange.set(min, max);
    }

    public void setVelocitySplits(float x, float y) {
        velocitySplits.set(x, y);
    }

    public void setAngularVelocityRange(float min, float max) {
        angularVelocityRange.set(min, max);
    }

    public void setRedRange(float min, float max) {
        redRange.set(min, max);
    }

    public void setGreenRange(float min, float max) {
        greenRange.set(min, max);
    }

    public void setBlueRange(float min, float max) {
        blueRange.set(min, max);
    }

    public void setAlphaRange(float min, float max) {
        alphaRange.set(min, max);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSizeRange() {
        return sizeRange;
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public Array<Particle> getParticles() {
        return particles;
    }

    public Array<ParticleModifier> getModifiers() {
        return modifiers;
    }

    public Vector2 getDurationRange() {
        return durationRange;
    }

    public Vector2 getParticlesRange() {
        return particlesRange;
    }

    public Vector2 getXOffsetRange() {
        return xOffsetRange;
    }

    public Vector2 getYOffsetRange() {
        return yOffsetRange;
    }

    public Vector2 getVXRange() {
        return vxRange;
    }

    public Vector2 getVYRange() {
        return vyRange;
    }

    public Vector2 getVelocitySplits() {
        return velocitySplits;
    }

    public Vector2 getAngularVelocityRange() {
        return angularVelocityRange;
    }

    public Vector2 getRedRange() {
        return redRange;
    }

    public Vector2 getBlueRange() {
        return blueRange;
    }

    public Vector2 getGreenRange() {
        return greenRange;
    }

    public Vector2 getAlphaRange() {
        return alphaRange;
    }

    public Float getLoopDelay() {
        return loopDelay;
    }

    protected void load(JsonValue json) {
        if (json.has("loop_delay")) {
            loopDelay = json.getFloat("loop_delay");
        }
        if (json.has("textures")) {
            loadSprites(json.get("textures").asStringArray());
        }
        if (json.has("ranges")) {
            loadRanges(json.get("ranges"));
        }
        if (json.has("modifiers")) {
            loadModifiers(json.get("modifiers"));
        }
    }

    protected void loadSprites(String[] textureNames) {
        for (String textureName : textureNames) {
            Sprite sprite = AssetManager.getInstance().getSprite(textureName);
            sprites.add(sprite);
        }
    }

    protected void loadRanges(JsonValue root) {
        if (root.has("duration")) {
            durationRange.set(ConversionUtils.toVector2(root.get("duration")));
        }
        if (root.has("num_particles")) {
            particlesRange.set(ConversionUtils.toVector2(root.get("num_particles")));
        }
        if (root.has("position_offset")) {
            Vector2 minOffset = ConversionUtils.toVector2(root.get("position_offset").get(0));
            Vector2 maxOffset = ConversionUtils.toVector2(root.get("position_offset").get(1));
            xOffsetRange.set(minOffset.x, maxOffset.x);
            yOffsetRange.set(minOffset.y, maxOffset.y);
        }
        if (root.has("angular_velocity")) {
            angularVelocityRange.set(ConversionUtils.toVector2(root.get("angular_velocity")));
        }
        if (root.has("color")) {
            Color minColor = ConversionUtils.toColor(root.get("color").get(0));
            Color maxColor = ConversionUtils.toColor(root.get("color").get(1));
            redRange.set(minColor.r, maxColor.r);
            greenRange.set(minColor.g, maxColor.g);
            blueRange.set(minColor.b, maxColor.b);
            alphaRange.set(minColor.a, maxColor.a);
        }
        if (root.has("velocity")) {
            JsonValue velocityRoot = root.get("velocity");
            Vector2 minVelocity = ConversionUtils.toVector2(velocityRoot.get("range").get(0));
            Vector2 maxVelocity = ConversionUtils.toVector2(velocityRoot.get("range").get(1));
            vxRange.set(minVelocity.x, maxVelocity.x);
            vyRange.set(minVelocity.y, maxVelocity.y);
            velocitySplits.set(ConversionUtils.toVector2(velocityRoot.get("splits")));
        }
    }

    protected void loadModifiers(JsonValue root) {
        for (JsonValue modifierRoot : root) {
            ParticleModifier modifier =
                    ParticleEffectManager.getInstance().buildParticleModifier(modifierRoot);
            modifiers.add(modifier);
        }
    }

    protected void createParticles() {
        int numParticles = (int)RandomUtils.pickFromRange(particlesRange);
        for (int i = 0; i < numParticles; i++) {
            Particle particle = particlePool.obtain();
            setParticleProperties(particle);
            particles.add(particle);
        }
    }

    protected void setParticleProperties(Particle particle) {
        Sprite sprite = sprites.get(RandomUtils.pickIndex(sprites));
        particle.setSprite(sprite);

        float dx = RandomUtils.pickFromRange(xOffsetRange);
        float dy = RandomUtils.pickFromRange(yOffsetRange);
        particle.setPosition(position.x + dx, position.y + dy);

        float size = RandomUtils.pickFromRange(sizeRange);
        particle.setSize(size, size);

        float duration = RandomUtils.pickFromRange(durationRange);
        particle.setDuration(duration);

        float vx = RandomUtils.pickFromSplitRange(vxRange.x, vxRange.y, velocitySplits.x);
        float vy = RandomUtils.pickFromSplitRange(vyRange.x, vyRange.y, velocitySplits.y);
        particle.setVelocity(vx, vy);

        float angularVelocity = RandomUtils.pickFromRange(angularVelocityRange);
        particle.setAngularVelocity(angularVelocity);

        float r = RandomUtils.pickFromRange(redRange);
        float g = RandomUtils.pickFromRange(greenRange);
        float b = RandomUtils.pickFromRange(blueRange);
        float a = RandomUtils.pickFromRange(alphaRange);
        particle.setColor(r, g, b, a);

        particle.setStarted();
    }
}
