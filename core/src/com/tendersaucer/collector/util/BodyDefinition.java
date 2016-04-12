package com.tendersaucer.collector.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Body definition data class
 *
 * Created by Alex on 4/8/2016.
 */
public class BodyDefinition {

    public Vector2 size;
    public Vector2 position;
    public BodyDef.BodyType bodyType;

    public BodyDefinition() {
    }

    public BodyDefinition(Vector2 position, Vector2 size, BodyDef.BodyType bodyType) {
        this.position = position;
        this.size = size;
        this.bodyType = bodyType;
    }
}
