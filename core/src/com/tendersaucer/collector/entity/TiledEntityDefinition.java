package com.tendersaucer.collector.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tendersaucer.collector.util.BodyDefinition;
import com.tendersaucer.collector.util.TiledUtils;

/**
 * Entity definition based on Tiled map object and properties
 *
 * Created by Alex on 4/12/2016.
 */
public class TiledEntityDefinition extends EntityDefinition {

    private final MapProperties properties;
    private final FixtureDef fixtureDef;

    public TiledEntityDefinition(String name, BodyDefinition bodyDef, MapProperties properties, MapObject bodySkeleton) {
        super(name, bodyDef);

        this.properties = properties;

        fixtureDef = TiledUtils.getFixtureDefFromBodySkeleton(bodySkeleton);
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
