package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.level.Level;

/**
 * Main game camera
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class MainCamera implements IUpdate {

    private static final int TILES_PER_SCREEN_WIDTH = 18;
    private static final MainCamera instance = new MainCamera();
    private static final int TILE_SIZE_PIXELS = 64;
    private static final int BASE_VIEWPORT_WIDTH = 50; // 50m is small enough for Box2D to handle
    private static final int BASE_VIEWPORT_HEIGHT = 50;

    private boolean playerFocus = true;
    private final OrthographicCamera rawCamera;

    private MainCamera() {
        rawCamera = new OrthographicCamera();
        rawCamera.setToOrtho(true, BASE_VIEWPORT_WIDTH, BASE_VIEWPORT_HEIGHT);
        rawCamera.zoom += 0.2f; // TODO: Hack
    }

    public static MainCamera getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        if (playerFocus) {
            Player player = Level.getInstance().getPlayer();
            if (player != null) {
                rawCamera.position.set(player.getCenterX(), player.getCenterY(), 0);
            }
        }

        rawCamera.update();

        return false;
    }

    public OrthographicCamera getRawCamera() {
        return rawCamera;
    }

    public void setPosition(float centerX, float centerY) {
        rawCamera.position.x = centerX;
        rawCamera.position.y = centerY;
    }

    public void move(float dx, float dy) {
        rawCamera.position.x += dx;
        rawCamera.position.y += dy;
    }

    public void resizeViewport(float width, float height) {
        rawCamera.viewportHeight = (rawCamera.viewportWidth / width) * height;
        rawCamera.position.y = rawCamera.viewportHeight / 2;
        rawCamera.update();
    }

    public float getViewportWidth() {
        return rawCamera.viewportWidth;
    }

    public float getViewportHeight() {
        return rawCamera.viewportHeight;
    }

    public void flipHorizontally() {
        rawCamera.position.z *= -1;
        rawCamera.direction.z *= -1;
    }

    public float getTileMapScale() {
        float viewportTileSize = getViewportWidth() / TILES_PER_SCREEN_WIDTH;
        return viewportTileSize / TILE_SIZE_PIXELS;
    }

    public boolean isFlipped() {
        return rawCamera.direction.z < 0;
    }

    public float getCenterX() {
        return rawCamera.position.x;
    }

    public float getCenterY() {
        return rawCamera.position.y;
    }

    public float getLeft() {
        return getCenterX() - (getViewportWidth() / 2);
    }

    public float getTop() {
        return getCenterY() - (getViewportHeight() / 2);
    }

    public float getRight() {
        return getCenterX() + (getViewportWidth() / 2);
    }

    public float getBottom() {
        return getCenterY() + (getViewportHeight() / 2);
    }

    public float getTileSize() {
        return getViewportWidth() / TILES_PER_SCREEN_WIDTH;
    }

    public void setPlayerFocus(boolean playerFocus) {
        this.playerFocus = playerFocus;
    }
}

