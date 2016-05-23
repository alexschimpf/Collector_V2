package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 5/21/2016.
 */
public final class ConfigurableEntityDefinition extends EntityDefinition {

    private final Map<String, String> propertyMap;
    private int layer;
    private FixtureDef fixtureDef;
    private TextureRegion textureRegion;

    public ConfigurableEntityDefinition(String id, String type, BodyDef bodyDef) {
        super(id, type, bodyDef);

        propertyMap = new HashMap<String, String>();
    }

    @Override
    public Object getProperty(String key) {
        return propertyMap.get(key);
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void addPropety(String name, String value) {
        propertyMap.put(name, value);
    }

    public void removePropety(String name) {
        propertyMap.remove(name);
    }
}
