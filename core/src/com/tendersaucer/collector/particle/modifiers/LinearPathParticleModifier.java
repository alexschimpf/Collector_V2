package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.particle.Particle;
import com.tendersaucer.collector.util.JsonUtils;
import com.tendersaucer.collector.util.PathHelper;

/**
 * Created by Alex on 5/5/2016.
 */
public class LinearPathParticleModifier extends ParticleModifier {

    private PathHelper pathHelper;

    public LinearPathParticleModifier(JsonValue json) {
        super(json);
    }

    @Override
    public void modify(Particle particle) {
        Vector2 velocity = pathHelper.getVelocity(particle.getDuration(), particle.getAge());
        particle.setVelocity(velocity.x, velocity.y);
    }

    @Override
    protected void load(JsonValue json) {
        Array<Vector2> legs = new Array<Vector2>();
        for (JsonValue leg : json.get("legs")) {
            legs.add(JsonUtils.toVector2(leg));
        }

        pathHelper = new PathHelper(legs);
    }
}