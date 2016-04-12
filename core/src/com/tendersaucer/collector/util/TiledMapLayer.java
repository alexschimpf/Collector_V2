package com.tendersaucer.collector.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.tendersaucer.collector.IRender;

/**
 * Wrapper around TiledMapLayer
 *
 * Created by Alex on 4/12/2016.
 */
public final class TiledMapLayer implements IRender {

    private final TiledMapTileLayer rawLayer;

    public TiledMapLayer(TiledMapTileLayer rawLayer) {
        this.rawLayer = rawLayer;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Need to render with OrthogonalTiledMapRenderer?
    }

    public TiledMapTileLayer getRawLayer() {
        return rawLayer;
    }

    public boolean propertyExists(String key) {
        MapProperties properties = rawLayer.getProperties();
        return properties.containsKey(key);
    }

    public boolean isPropertyEmpty(String key) {
        MapProperties properties = rawLayer.getProperties();
        return !properties.containsKey(key) || properties.get(key).toString().isEmpty();
    }

    public Object getProperty(String key) {
        return rawLayer.getProperties().get(key);
    }

    public String getStringProperty(String key) {
        return getProperty(key).toString();
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getStringProperty(key));
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(getStringProperty(key));
    }

    public float getFloatProperty(String key) {
        return Float.parseFloat(getStringProperty(key));
    }
}
