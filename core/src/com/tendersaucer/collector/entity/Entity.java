package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.ICollide;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Abstract, top-level entity
 *
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, IRender, ICollide {

    public Entity(EntityDefinition def) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {
    }

    public void onCreate(EntityDefinition entityDef) {
    }
}
