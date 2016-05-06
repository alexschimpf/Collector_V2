package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alex on 5/5/2016.
 */
public class Path {

    private float totalDistance;

    // Each vector represents a "leg" (i.e. (dx, dy)) of the path.
    private final Array<Vector2> points;

    public Path() {
        points = new Array<Vector2>();
    }

    public Path(Array<Vector2> points) {
        this.points = points;
        setPath(points);
    }

    public Vector2 getVelocity(float duration, float age) {
        duration /= 1000;
        age /= 1000;

        float speed = totalDistance / duration;
        float distanceCovered = speed * age;

        Vector2 velocity = new Vector2();
        for (int i = 1; i < points.size; i++) {
            Vector2 lastPoint = points.get(i - 1);
            Vector2 currPoint = points.get(i);

            distanceCovered -= MiscUtils.dist(lastPoint, currPoint);
            if (distanceCovered < 0) {
                float theta = currPoint.angleRad(lastPoint);
                velocity.set(MathUtils.cos(theta), MathUtils.sin(theta)).scl(speed);
                break;
            }
        }

        return velocity;
    }

    public void setPath(Array<Vector2> points) {
        this.points.clear();
        this.points.add(new Vector2(0, 0));
        this.points.addAll(points);

        totalDistance = 0;
        for (int i = 1; i < this.points.size; i++) {
            totalDistance += MiscUtils.dist(this.points.get(i-1), this.points.get(i));
        }
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public float getTotalDistance() {
        return totalDistance;
    }
}
