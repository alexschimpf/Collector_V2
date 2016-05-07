package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alex on 5/5/2016.
 */
public class PathHelper {

    private float totalDistance;

    // Each vector represents a "leg" (i.e. (dx, dy)) of the path.
    private final Array<Vector2> legs;

    public PathHelper() {
        legs = new Array<Vector2>();
    }

    public PathHelper(Array<Vector2> legs) {
        this();

        setPath(legs);
    }

    public Vector2 getVelocity(float duration, float age) {
        // Keep time in seconds.
        duration /= 1000;
        age /= 1000;

        float speed = totalDistance / duration;
        float distanceCovered = speed * age;

        Vector2 velocity = new Vector2();
        for (Vector2 leg : legs) {
            distanceCovered -= leg.len();
            if (distanceCovered < 0) {
                float theta = leg.angleRad();
                velocity.set(MathUtils.cos(theta), MathUtils.sin(theta)).scl(speed);
                break;
            }
        }

        return velocity;
    }

    public void setPath(Array<Vector2> legs) {
        this.legs.clear();
        this.legs.add(new Vector2(0, 0));
        this.legs.addAll(legs);

        totalDistance = 0;
        for (int i = 0; i < this.legs.size; i++) {
            totalDistance += this.legs.get(i).len();
        }
    }

    public Array<Vector2> getLegs() {
        return legs;
    }

    public float getTotalDistance() {
        return totalDistance;
    }
}
