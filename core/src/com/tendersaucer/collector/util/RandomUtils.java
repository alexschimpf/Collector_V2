package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.List;

/**
 * Utility functions for randomness
 *
 * Created by Alex on 4/28/2016.
 */
public final class RandomUtils {

    public static boolean pick(boolean a, boolean b) {
        return MathUtils.random() < 0.5f ? a : b;
    }

    public static float pick(float a, float b) {
        return MathUtils.random() < 0.5f ? a : b;
    }

    public static int pick(int a, int b) {
        return MathUtils.random() < 0.5f ? a : b;
    }

    public static String pick(String a, String b) {
        return MathUtils.random() < 0.5f ? a : b;
    }

    public static int pickIndex(Object... objects) {
        return MathUtils.random(0, objects.length - 1);
    }

    public static int pickIndex(List list) {
        return MathUtils.random(0, list.size() - 1);
    }

    public static int pickIndex(Array array) {
        return MathUtils.random(0, array.size - 1);
    }

    public static float pickFromRange(float a, float b) {
        return MathUtils.random(a, b);
    }

    public static int pickFromRange(int a, int b) {
        return MathUtils.random(a, b);
    }

    public static float pickFromRange(Vector2 range) {
        return pickFromRange(range.x, range.y);
    }

    public static float pickFromSplitRange(Vector2 range, float split) {
        return pickFromSplitRange(range.x, range.y, split);
    }

    public static float pickFromSplitRange(float a, float b, float split) {
        if (split == 0) {
            return pickFromRange(a, b);
        }

        split = Math.abs(split);
        return pick(pickFromRange(a, -split), pickFromRange(split, b));
    }

    public static float pickFromRanges(Vector2... ranges) {
        Vector2 range = ranges[pickIndex(ranges)];
        return pickFromRange(range);
    }
}
