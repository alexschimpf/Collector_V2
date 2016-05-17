package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.particle.Particle;
import com.tendersaucer.collector.util.ConversionUtils;

/**
 * Created by Alex on 4/30/2016.
 */
public class LinearSizeParticleModifier extends ParticleModifier {

    protected Vector2 scale;

    public LinearSizeParticleModifier(JsonValue json) {
        super(json);
    }

    @Override
    public void modify(Particle particle) {
        float ageToLifeRatio = particle.getAgeToLifeRatio();
        float scaleX = interpolate(1, scale.x, ageToLifeRatio);
        float scaleY = interpolate(1, scale.y, ageToLifeRatio);
        particle.setScale(scaleX, scaleY);
    }

    @Override
    protected void load(JsonValue json) {
        scale = ConversionUtils.toVector2(json.get("scale"));
    }
}
