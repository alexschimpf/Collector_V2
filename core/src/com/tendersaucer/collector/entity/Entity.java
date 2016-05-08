package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IDisposable;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.world.ICollide;
import com.tendersaucer.collector.world.room.Room;

/**
 * Abstract entity
 *
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, IRender, ICollide, IDisposable {

    public enum State {
        ACTIVE, INACTIVE, DONE
    }

    protected Body body;
    protected String type;
    protected Sprite sprite;
    protected State state;
    protected final String id;

    public Entity(EntityDefinition def) {
        this.type = def.getType();

        String defId = def.getId();
        if (defId == null || defId.isEmpty()) {
            id = Room.getInstance().getAvailableEntityId();
        } else {
            id = defId;
        }

        state = State.ACTIVE;
    }

    /**
     * For any necessary post-construction operations (e.g. listening to events)
     * @param entityDef
     */
    public void init(EntityDefinition entityDef) {
    }

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(Player.TYPE);
    }

    protected abstract void tick();

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        if (isDone()) {
            dispose();
        } else {
            tick();
        }

        return isDone();
    }

    @Override
    public void dispose() {
        Globals.getPhysicsWorld().destroyBody(body);
        Canvas.getInstance().remove(this);
    }

    public State getState() {
        return state;
    }

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public boolean isInactive() {
        return state == State.INACTIVE;
    }

    public boolean isDone() {
        return state == State.DONE;
    }

    public void setDone() {
        state = State.DONE;
    }

    public void setActive(boolean active) {
        state = active ? State.ACTIVE : State.INACTIVE;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Body getBody() {
        return body;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
