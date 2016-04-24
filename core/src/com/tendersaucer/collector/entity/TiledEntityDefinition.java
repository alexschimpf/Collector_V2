package com.tendersaucer.collector.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tendersaucer.collector.util.TiledUtils;

/**
 * Entity definition also based on Tiled map objects and properties
 *
 * Created by Alex on 4/12/2016.
 */
public final class TiledEntityDefinition extends EntityDefinition {

    private final int layer;
    private final MapProperties properties;
    private final MapObject bodySkeleton;

    public TiledEntityDefinition(String name, String type, int layer, BodyDef bodyDef,
                                 MapObject bodySkeleton, MapProperties properties) {
        super(name, type, bodyDef);

        this.layer = layer;
        this.properties = properties;
        this.bodySkeleton = bodySkeleton;
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public FixtureDef getFixtureDef() {
        return TiledUtils.getFixtureDefFromBodySkeleton(bodySkeleton);
    }

    @Override
    public int getLayer() {
        return layer;
    }
}
