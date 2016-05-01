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
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.util.JsonUtils;
import com.tendersaucer.collector.util.RandomUtils;

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

    protected long lastLoopTime;
    protected Float loopDelay;
    protected final Vector2 position;
    protected final Vector2 sizeRange;
    protected final Array<Sprite> sprites;
    protected final Array<Particle> particles;
    protected final Array<ParticleModifier> modifiers;

    // Ranges
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

    public ParticleEffect(JsonValue json) {
        position = new Vector2();
        sizeRange = new Vector2();
        modifiers = new Array<ParticleModifier>();
        particles = new Array<Particle>();
        sprites = new Array<Sprite>();
        lastLoopTime = 0;

        durationRange = new Vector2();
        particlesRange = new Vector2();
        xOffsetRange = new Vector2();
        yOffsetRange = new Vector2();
        vxRange = new Vector2();
        vyRange = new Vector2();
        velocitySplits = new Vector2();
        angularVelocityRange = new Vector2();
        redRange = new Vector2();
        blueRange = new Vector2();
        greenRange = new Vector2();
        alphaRange = new Vector2();

        load(json);
    }

    @Override
    public void dispose() {
        Canvas.getInstance().remove(this);
        for (Particle particle : particles) {
            particlePool.free(particle);
        }
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
                    modifier.modify(particle);
                }
            }
        }

        if (loops() && TimeUtils.timeSinceMillis(lastLoopTime) > loopDelay) {
            lastLoopTime = TimeUtils.millis();
            createParticles();
        }

        return particles.size <= 0;
    }

    public void begin(Vector2 position, Vector2 sizeRange, int layer) {
        this.position.set(position);
        this.sizeRange.set(sizeRange);

        createParticles();

        Canvas.getInstance().addToLayer(layer, this);
    }

    public boolean loops() {
        return loopDelay != null;
    }

    protected void load(JsonValue json) {
        if (json.has("loop_delay")) {
            loopDelay = json.getFloat("loop_delay");
        }
        if (json.has("texture_keys")) {
            loadSprites(json.get("texture_keys").asStringArray());
        }
        if (json.has("ranges")) {
            loadRanges(json.get("ranges"));
        }
        if (json.has("modifiers")) {
            loadModifiers(json.get("modifiers"));
        }
    }

    protected void loadSprites(String[] textureKeys) {

    }

    protected void loadRanges(JsonValue root) {
        if (root.has("duration")) {
            durationRange.set(JsonUtils.toVector2(root.get("duration")));
        }
        if (root.has("num_particles")) {
            particlesRange.set(JsonUtils.toVector2(root.get("num_particles")));
        }
        if (root.has("position_offset")) {
            Vector2 minOffset = JsonUtils.toVector2(root.get("position_offset").get(0));
            Vector2 maxOffset = JsonUtils.toVector2(root.get("position_offset").get(1));
            xOffsetRange.set(minOffset.x, maxOffset.x);
            yOffsetRange.set(minOffset.y, maxOffset.y);
        }
        if (root.has("angular_velocity")) {
            angularVelocityRange.set(JsonUtils.toVector2(root.get("angular_velocity")));
        }
        if (root.has("color")) {
            Color minColor = JsonUtils.toColor(root.get("color").get(0));
            Color maxColor = JsonUtils.toColor(root.get("color").get(1));
            redRange.set(minColor.r, maxColor.r);
            greenRange.set(minColor.g, maxColor.g);
            blueRange.set(minColor.b, maxColor.b);
            alphaRange.set(minColor.a, maxColor.a);
        }
        if (root.has("velocity")) {
            JsonValue velocityRoot = root.get("velocity");
            Vector2 minVelocity = JsonUtils.toVector2(velocityRoot.get("range").get(0));
            Vector2 maxVelocity = JsonUtils.toVector2(velocityRoot.get("range").get(1));
            vxRange.set(minVelocity.x, maxVelocity.x);
            vyRange.set(minVelocity.y, maxVelocity.y);
            velocitySplits.set(JsonUtils.toVector2(velocityRoot.get("splits")));
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
        position.add(dx, dy);
        particle.setPosition(position.x, position.y);

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

        particle.setReady();
    }
}
