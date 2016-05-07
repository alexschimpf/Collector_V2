package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.world.ICollide;
import com.tendersaucer.collector.IDisposable;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Abstract entity
 *
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, IRender, ICollide, IDisposable {

    public static final String PLAYER_TYPE = "player";

    protected boolean isDone;
    protected Body body;
    protected String type;
    protected Sprite sprite;

    public Entity(EntityDefinition def) {
        this.type = def.getType();

        isDone = false;
    }

    /**
     * For any necessary post-construction operations (e.g. listening to events)
     * @param entityDef
     */
    public void init(EntityDefinition entityDef) {
    }

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(PLAYER_TYPE);
    }

    protected abstract void tick();

    public String getType() {
        return type;
    }

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

    public String getId() {
        return null;
    }

    public void setDone() {
        isDone = true;
    }
}
