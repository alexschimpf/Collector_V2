package com.tendersaucer.collector.particle.modifiers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.particle.Particle;

/**
 * Created by Alex on 5/5/2016.
 */
public class SineVelocityParticleModifier extends ParticleModifier {

    protected float xAmplitude, yAmplitude;
    protected float xHShift, yHShift;
    protected float xVShift, yVShift;
    protected float xNumCycles, yNumCycles;

    public SineVelocityParticleModifier(JsonValue json) {
        super(json);
    }

    @Override
    public void modify(Particle particle) {
        float startTime = TimeUtils.timeSinceMillis(particle.getStartTime());
        float vx = xAmplitude * MathUtils.sin(xNumCycles * (startTime - xHShift)) + xVShift;
        float vy = yAmplitude * MathUtils.sin(yNumCycles * (startTime - yHShift)) + yVShift;
        particle.setVelocity(vx, vy);
    }

    @Override
    protected void load(JsonValue json) {
        JsonValue xRoot = json.get("x");
        xAmplitude = xRoot.getFloat("amplitude");
        xHShift = xRoot.getFloat("h_shift");
        xVShift = xRoot.getFloat("v_shift");
        xNumCycles = xRoot.getFloat("num_cycles");

        JsonValue yRoot = json.get("y");
        yAmplitude = yRoot.getFloat("amplitude");
        yHShift = yRoot.getFloat("h_shift");
        yVShift = yRoot.getFloat("v_shift");
        yNumCycles = yRoot.getFloat("num_cycles");
    }
}
