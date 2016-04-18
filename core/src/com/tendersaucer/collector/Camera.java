package com.tendersaucer.collector;

/**
 * Main game camera
 *
 * Created by Alex on 4/8/2016.
 */
public final class Camera implements IUpdate {

    private static final Camera instance = new Camera();
    public static final int SCREEN_NUM_TILES_WIDE = 18;

    private Camera() {
    }

    public static Camera getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {
    }

    public com.badlogic.gdx.graphics.Camera getRawCamera() {
        return null;
    }

    public void resizeViewport(int width, int height) {
    }

    public float getViewportWidth() {
        return 0;
    }

    public float getViewportHeight() {
        return 0;
    }

    public float getTileMapScale() {
        return 0;
    }
}

