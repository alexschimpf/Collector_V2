package com.tendersaucer.collector.util;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Convenience class that simply holds a FixtureDef and BodyDef
 *
 * Created by Alex on 4/24/2016.
 */
public class FixtureBodyDefinition {

    public FixtureDef fixtureDef;
    public BodyDef bodyDef;

    public FixtureBodyDefinition() {
    }

    public FixtureBodyDefinition(FixtureDef fixtureDef, BodyDef bodyDef) {
        this.fixtureDef = fixtureDef;
        this.bodyDef = bodyDef;
    }
}
