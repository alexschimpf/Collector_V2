package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tendersaucer.collector.MainCamera;

/**
 * Created by Alex on 5/5/2016.
 */
public final class ConversionUtils {

    private ConversionUtils() {
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
        MainCamera camera = MainCamera.getInstance();
        Vector3 coords = Vector3Pool.getInstance().obtain(x, y, 0);
        camera.getRawCamera().unproject(coords);
        Vector3Pool.getInstance().free(coords);
        return new Vector2(coords.x, camera.getViewportHeight() - coords.y);
    }

    public static Vector2 toScreenCoords(float x, float y) {
        MainCamera camera = MainCamera.getInstance();
        Vector3 coords = Vector3Pool.getInstance().obtain(x, y, 0);
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
