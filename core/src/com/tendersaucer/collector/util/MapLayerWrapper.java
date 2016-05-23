package com.tendersaucer.collector.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tendersaucer.collector.screen.IRender;

/**
 * Wrapper around TiledMapLayer
 * <p/>
 * Created by Alex on 4/12/2016.
 */
public final class MapLayerWrapper implements IRender {

    private final MapLayer rawLayer;
    private final OrthogonalTiledMapRenderer renderer;

    public MapLayerWrapper(OrthogonalTiledMapRenderer renderer, MapLayer rawLayer) {
        this.renderer = renderer;

        if (rawLayer instanceof TiledMapTileLayer) {
            flipCells((TiledMapTileLayer)rawLayer);
        }
        this.rawLayer = rawLayer;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        renderer.renderTileLayer((TiledMapTileLayer)rawLayer);
    }

    public MapProperties getProperties() {
        return rawLayer.getProperties();
    }

    public String getName() {
        return rawLayer.getName();
    }

    public MapObjects getObjects() {
        return rawLayer.getObjects();
    }

    private void flipCells(TiledMapTileLayer rawLayer) {
        for (int x = 0; x < rawLayer.getWidth(); x++) {
            for (int y = 0; y < rawLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = rawLayer.getCell(x, y);
                if (cell != null) {
                    cell.setFlipVertically(true);
                }
            }
        }
    }
}
