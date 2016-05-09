package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IDisposable;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.MiscUtils;
import com.tendersaucer.collector.util.Vector2Pool;
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

    protected State state;
    protected Body body;
    protected final String type;
    protected final String id;
    protected final Vector2 leftTop;
    protected final Rectangle bounds;
    protected final EntityDefinition definition; // in case it needs to be cloned

    public Entity(EntityDefinition definition) {
        this.definition = definition;
        this.type = definition.getType();

        String defId = definition.getId();
        if (defId == null || defId.isEmpty()) {
            id = Room.getInstance().getAvailableEntityId();
        } else {
            id = defId;
        }

        state = State.ACTIVE;

        float centerX = definition.getPosition().x;
        float centerY = definition.getPosition().y;
        float width = definition.getSize().x;
        float height = definition.getSize().y;
        bounds = new Rectangle(MiscUtils.getLeft(centerX, width),
                MiscUtils.getBottom(centerY, height), width, height);
        leftTop = Vector2Pool.getInstance().obtain(MiscUtils.getLeft(centerX, width),
                MiscUtils.getTop(centerY, height));
    }

    /**
     * For any necessary post-construction operations (e.g. listening to events)
     * @param entityDef
     */
    public void init(EntityDefinition entityDef) {
    }

    @Override
    public void onBeginContact(Contact contact, Entity other) {
    }

    @Override
    public void onEndContact(Contact contact, Entity other) {
    }

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
    }

    public void addToCanvas() {
        Canvas.getInstance().addToLayer(definition.getLayer(), this);
    }

    public void setTopLeft(float left, float top) {
        setCenter(left + getWidth(), top + getHeight());
    }

    public void setCenter(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }

    public void setAngle(float angle) {
        body.setTransform(getCenterX(), getCenterY(), angle);
    }

    public void rotateDegrees(float degrees) {
        rotateRadians(MathUtils.radiansToDegrees * degrees);
    }

    public void rotateRadians(float radians) {
        body.setTransform(getCenterX(), getCenterY(), body.getAngle() + radians);
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

    public float getAngle() {
        return body.getAngle();
    }

    public float getWidth() {
        return bounds.getWidth();
    }

    public float getHeight() {
        return bounds.getHeight();
    }

    public float getLeft() {
        return bounds.getX();
    }

    public float getRight() {
        return MiscUtils.getRight(bounds.getX(), bounds.getWidth());
    }

    public float getTop() {
        return MiscUtils.getTop(bounds.getY(), bounds.getHeight());
    }

    public float getBottom() {
        return bounds.getY();
    }

    public float getCenterX() {
        return MiscUtils.getCenterX(bounds.getX(), bounds.getWidth());
    }

    public float getCenterY() {
        return MiscUtils.getCenterY(bounds.getY(), bounds.getHeight());
    }

    public Vector2 getLeftTop() {
        return leftTop;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean overlaps(Entity entity) {
        return bounds.overlaps(entity.getBounds());
    }

    protected void tick() {
        float centerX = body.getPosition().x;
        float centerY = body.getPosition().y;
        float left = MiscUtils.getLeft(centerX, getWidth());
        float top = MiscUtils.getTop(centerY, getHeight());
        bounds.setPosition(left, top + getHeight());
        leftTop.set(left, top);
    }
}
