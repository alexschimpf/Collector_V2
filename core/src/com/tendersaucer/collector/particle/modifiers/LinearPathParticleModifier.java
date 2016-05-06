package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.particle.Particle;
import com.tendersaucer.collector.util.JsonUtils;
import com.tendersaucer.collector.util.Path;

/**
 * Created by Alex on 5/5/2016.
 */
public class LinearPathParticleModifier extends ParticleModifier {

    private Path path;

    public LinearPathParticleModifier(JsonValue json) {
        super(json);
    }

    @Override
    public void modify(Particle particle) {
        Vector2 velocity = path.getVelocity(particle.getDuration(), particle.getAge());
        particle.setVelocity(velocity.x, velocity.y);
    }

    @Override
    protected void load(JsonValue json) {
        path = new Path();
        Array<Vector2> points = new Array<Vector2>();
        for (JsonValue pointVal : json.get("points")) {
            points.add(JsonUtils.toVector2(pointVal));
        }

        path.setPath(points);
    }
}
