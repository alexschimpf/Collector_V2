package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Collection of miscellaneous utility functions
 *
 * Created by Alex on 4/28/2016.
 */
public final class MiscUtils {

    private MiscUtils() {
    }

    public static float interpolate(float start, float end, float ratio) {
        return start + ((end - start) * ratio);
    }

    public static float bound(float val, float lower, float upper) {
        return val < lower ? lower : (val > upper ? upper : val);
    }

    public static float dist(Vector2 start, Vector2 end) {
        return Vector2.dst(start.x, start.y, end.x, end.y);
    }

    public static float angleRad(Vector2 a, Vector2 b) {
        return a.angleRad(b);
    }
}
