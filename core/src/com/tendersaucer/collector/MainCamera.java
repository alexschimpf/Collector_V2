package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Main game camera
 *
 * Created by Alex on 4/8/2016.
 */
public final class MainCamera implements IUpdate {

    private static final MainCamera instance = new MainCamera();
    private static final int TILE_SIZE_PIXELS = 64;
    private static final int BASE_VIEWPORT_WIDTH = 50; // 50m is small enough for Box2 to handle
    private static final int BASE_VIEWPORT_HEIGHT = 50;
    public static final int NUM_TILES_PER_SCREEN_WIDTH = 18;

    private final OrthographicCamera rawCamera;

    private MainCamera() {
        rawCamera = new OrthographicCamera();
        rawCamera.setToOrtho(true, BASE_VIEWPORT_WIDTH, BASE_VIEWPORT_HEIGHT);
    }

    public static MainCamera getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        // TODO: Center camera on player!
        rawCamera.update();

        return false;
    }

    public OrthographicCamera getRawCamera() {
        return rawCamera;
    }

    public void resizeViewport(float width, float height) {
        rawCamera.viewportHeight = (rawCamera.viewportWidth / width) * height;
        rawCamera.position.y = rawCamera.viewportHeight / 2; // TODO: Remove this!
        rawCamera.update();
    }

    public float getViewportWidth() {
        return rawCamera.viewportWidth;
    }

    public float getViewportHeight() {
        return rawCamera.viewportHeight;
    }

    public float getTileMapScale() {
        float viewportTileSize = getViewportWidth() / NUM_TILES_PER_SCREEN_WIDTH;
        return viewportTileSize / TILE_SIZE_PIXELS;
    }
}

