package com.tendersaucer.collector.util;

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
}
