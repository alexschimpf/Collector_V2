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
        for (int col = 0; col < rawLayer.getWidth(); col++) {
            for (int row = 0; row < rawLayer.getHeight(); row++) {
                TiledMapTileLayer.Cell cell = rawLayer.getCell(col, row);
                if (cell != null) {
                    cell.setFlipVertically(true);
                }
            }
        }
    }
}
