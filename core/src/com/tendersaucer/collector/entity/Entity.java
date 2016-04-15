package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.ICollide;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.world.IRoomLoadable;

/**
 * Abstract entity
 *
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, IRender, ICollide {

    public Entity(EntityDefinition def) {

    }

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(IRoomLoadable.PLAYER_TYPE);
    }

    public abstract String getType();

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

    public String getId() {
        return null;
    }
}
