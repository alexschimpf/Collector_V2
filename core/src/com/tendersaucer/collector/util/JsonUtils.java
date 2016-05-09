package com.tendersaucer.collector.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Alex on 4/30/2016.
 */
public final class JsonUtils {

    public static Vector2 toVector2(JsonValue jsonVal) {
        float[] components = jsonVal.asFloatArray();
        return new Vector2(components[0], components[1]);
    }

    public static Color toColor(JsonValue jsonVal) {
        float[] rgb = jsonVal.asFloatArray();
        return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
    }
}
