package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.particle.Particle;
import com.tendersaucer.collector.util.JsonUtils;
import com.tendersaucer.collector.util.MiscUtils;

/**
 * Created by Alex on 4/30/2016.
 */
public class LinearColorParticleModifier extends ParticleModifier {

    protected Color startColor;
    protected Color endColor;

    public LinearColorParticleModifier(JsonValue json) {
        super(json);
    }

    @Override
    public void modify(Particle particle) {
        float ageToLifeRatio = particle.getAgeToLifeRatio();
        float r = MiscUtils.interpolate(startColor.r, endColor.r, ageToLifeRatio);
        float g = MiscUtils.interpolate(startColor.g, endColor.g, ageToLifeRatio);
        float b = MiscUtils.interpolate(startColor.b, endColor.b, ageToLifeRatio);
        float a = MiscUtils.interpolate(startColor.a, endColor.a, ageToLifeRatio);
        particle.setColor(r, g, b, a);
    }

    @Override
    protected void load(JsonValue json) {
        startColor = JsonUtils.toColor(json.get("start"));
        endColor = JsonUtils.toColor(json.get("end"));
    }
}
