package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tendersaucer.collector.Camera;

/**
 * Created by Alex on 5/5/2016.
 */
public final class ConversionUtils {

    private ConversionUtils() {
    }

    public static Vector2 toWorldCoords(float x, float y) {
        Vector3 screenCoords = Vector3Pool.getInstance().obtain(x, y, 0);
        Vector3 worldCoords = Camera.getInstance().getRawCamera().unproject(screenCoords);
        Vector3Pool.getInstance().free(screenCoords);
        return new Vector2(worldCoords.x, worldCoords.y);
    }

    public static Vector2 toScreenCoords(float x, float y) {
        Vector3 worldCoords = Vector3Pool.getInstance().obtain(x, y, 0);
        Vector3 screenCoords = Camera.getInstance().getRawCamera().project(worldCoords);
        Vector3Pool.getInstance().free(worldCoords);
        return new Vector2(screenCoords.x, screenCoords.y);
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
