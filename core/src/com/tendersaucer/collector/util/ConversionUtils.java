package com.tendersaucer.collector.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.MainCamera;

/**
 * Created by Alex on 5/5/2016.
 */
public final class ConversionUtils {

    private ConversionUtils() {
    }

    public static float ms2s(float ms) {
        return ms / 1000;
    }

    public static float s2ms(float s) {
        return s * 1000;
    }

    public static Vector2 toVector2(JsonValue jsonVal) {
        float[] components = jsonVal.asFloatArray();
        return new Vector2(components[0], components[1]);
    }

    public static Color toColor(JsonValue jsonVal) {
        float[] rgb = jsonVal.asFloatArray();
        return new Color(rgb[0], rgb[1], rgb[2], rgb[3]);
    }

    public static Color toColor(String string) {
        String[] rgbaStrings = string.split(",");
        float[] rgba = new float[rgbaStrings.length];
        for (int i = 0; i < rgba.length; i++) {
            rgba[i] = Float.parseFloat(rgbaStrings[i]);
        }

        return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static Vector2 toVector2(String str) {
        String[] pieces = str.split(", ");
        return new Vector2(Float.parseFloat(pieces[0]), Float.parseFloat(pieces[1]));
    }

    public static Vector2 toWorldCoords(float x, float y, float width, float height) {
        return toWorldCoords(x + (width / 2), y + (height / 2));
    }

    public static Vector2 toScreenCoords(float x, float y, float width, float height) {
        return toScreenCoords(x + (width / 2), y + (height / 2));
    }

    public static Vector2 toWorldCoords(float x, float y, float size) {
        return toWorldCoords(x, y, size, size);
    }

    public static Vector2 toScreenCoords(float x, float y, float size) {
        return toScreenCoords(x, y, size, size);
    }

    public static Vector2 toWorldCoords(float x, float y) {
        Vector3 coords = Vector3Pool.getInstance().obtain(x, y, 0);
        MainCamera camera = MainCamera.getInstance();
        camera.getRawCamera().unproject(coords);
        Vector3Pool.getInstance().free(coords);
        return new Vector2(coords.x, camera.getViewportHeight() - coords.y);
    }

    public static Vector2 toScreenCoords(float x, float y) {
        Vector3 coords = Vector3Pool.getInstance().obtain(x, y, 0);
        MainCamera camera = MainCamera.getInstance();
        camera.getRawCamera().project(coords);
        Vector3Pool.getInstance().free(coords);
        return new Vector2(coords.x, camera.getViewportHeight() - coords.y);
    }

    public static Vector2 toWorldCoords(Vector2 screenCoords) {
        return toWorldCoords(screenCoords.x, screenCoords.y);
    }

    public static Vector2 toScreenCoords(Vector2 worldCoords) {
        return toScreenCoords(worldCoords.x, worldCoords.y);
    }

    public static float toWorldX(float screenX) {
        return toWorldCoords(screenX, 0).x;
    }

    public static float toWorldY(float screenY) {
        return toWorldCoords(0, screenY).y;
    }

    public static float toScreenX(float worldX) {
        return toScreenCoords(worldX, 0).x;
    }

    public static float toScreenY(float worldY) {
        return toScreenCoords(0, worldY).y;
    }
}
