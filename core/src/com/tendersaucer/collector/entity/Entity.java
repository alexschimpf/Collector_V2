package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.ICollide;
import com.tendersaucer.collector.IDisposable;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.world.room.IRoomLoadable;

/**
 * Abstract entity
 *
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, IRender, ICollide, IDisposable {

    protected boolean isDone;
    protected Body body;
    protected Sprite sprite;

    public Entity(EntityDefinition def) {
        isDone = false;
    }

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(IRoomLoadable.PLAYER_TYPE);
    }

    public abstract String getType();

    protected abstract void tick();

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        if (isDone) {
            dispose();
        } else {
            tick();
        }

        return isDone;
    }

    @Override
    public void dispose() {
        Globals.getPhysicsWorld().destroyBody(body);
        Canvas.getInstance().remove(this);
    }

    public void onCreate(EntityDefinition entityDef) {
    }

    public String getId() {
        return null;
    }

    public void setDone() {
        isDone = true;
    }
}
