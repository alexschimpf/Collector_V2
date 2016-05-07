package com.tendersaucer.collector.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.tendersaucer.collector.screen.IRender;

/**
 * Wrapper around TiledMapLayer
 *
 * Created by Alex on 4/12/2016.
 */
public final class TiledMapLayer implements IRender {

    private final MapLayer rawLayer;

    public TiledMapLayer(MapLayer rawLayer) {
        this.rawLayer = rawLayer;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Need to render with OrthogonalTiledMapRenderer?
    }

    public MapLayer getRawLayer() {
        return rawLayer;
    }

    public String getName() {
        return rawLayer.getName();
    }

    public MapObjects getObjects() {
        return rawLayer.getObjects();
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
        if (!rawLayer.getProperties().containsKey(key)) {
            return null;
        }

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

    public boolean propertyExists(MapObject object, String key) {
        return object.getProperties().containsKey(key);
    }

    public boolean isPropertyEmpty(MapObject object, String key) {
        MapProperties properties = object.getProperties();
        return !properties.containsKey(key) || properties.get(key).toString().isEmpty();
    }

    public Object getProperty(MapObject object, String key) {
        if (!propertyExists(object, key)) {
            return null;
        }

        return object.getProperties().get(key);
    }

    public String getStringProperty(MapObject object, String key) {
        if (!propertyExists(object, key)) {
            return null;
        }

        return getProperty(object, key).toString();
    }

    public boolean getBooleanProperty(MapObject object, String key) {
        return Boolean.parseBoolean(getStringProperty(object, key));
    }

    public int getIntProperty(MapObject object, String key) {
        return Integer.parseInt(getStringProperty(object, key));
    }

    public float getFloatProperty(MapObject object, String key) {
        return Float.parseFloat(getStringProperty(object, key));
    }
}
