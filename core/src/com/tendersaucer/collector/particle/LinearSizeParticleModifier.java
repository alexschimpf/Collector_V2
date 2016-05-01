package com.tendersaucer.collector.particle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.util.JsonUtils;
import com.tendersaucer.collector.util.MiscUtils;

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
        float scaleX = MiscUtils.interpolate(1, scale.x, ageToLifeRatio);
        float scaleY = MiscUtils.interpolate(1, scale.y, ageToLifeRatio);
        particle.getSprite().setScale(scaleX, scaleY);
    }

    @Override
    protected void load(JsonValue json) {
        scale = JsonUtils.toVector2(json.get("scale"));
    }
}
