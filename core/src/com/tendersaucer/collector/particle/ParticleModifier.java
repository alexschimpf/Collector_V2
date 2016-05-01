package com.tendersaucer.collector.particle;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Alex on 4/30/2016.
 */
public abstract class ParticleModifier {

    public ParticleModifier(JsonValue root) {
        load(root);
    }

    public abstract void modify(Particle particle);

    protected abstract void load(JsonValue json);
}
