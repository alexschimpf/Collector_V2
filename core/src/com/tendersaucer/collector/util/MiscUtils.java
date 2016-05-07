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
}
