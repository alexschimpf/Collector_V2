package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.particle.Particle;

/**
 * Created by Alex on 4/30/2016.
 */
public abstract class ParticleModifier {

    public ParticleModifier(JsonValue root) {
        load(root);
    }

    public abstract void modify(Particle particle);

    protected abstract void load(JsonValue json);

    protected final float interpolate(float start, float end, float ratio) {
        return start + ((end - start) * ratio);
    }
}
