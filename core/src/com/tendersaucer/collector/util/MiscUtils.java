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

    public static float getCenterX(float left, float width) {
        return left + (width / 2);
    }

    public static float getCenterY(float top, float height) {
        return top - (height / 2);
    }

    public static float getTop(float centerY, float height) {
        return centerY - (height / 2);
    }

    public static float getLeft(float centerX, float width) {
        return centerX - (width / 2);
    }

    public static float getBottom(float centerY, float height) {
        return centerY + (height / 2);
    }

    public static float getRight(float centerX, float width) {
        return centerX + (width / 2);
    }
}
