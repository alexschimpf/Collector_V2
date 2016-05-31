package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tendersaucer.collector.IDisposable;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.gen.EntityConstants;
import com.tendersaucer.collector.level.ICollide;
import com.tendersaucer.collector.level.Level;
import com.tendersaucer.collector.util.BodyData;
import com.tendersaucer.collector.util.Vector2Pool;

import java.util.UUID;

/**
 * Abstract entity
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public abstract class Entity implements IUpdate, ICollide, IDisposable {

    public enum State {
        ACTIVE, INACTIVE, DONE
    }
    protected State state;
    protected Body body;
    protected String type;
    protected String id;
    protected Vector2 leftTop;
    protected Rectangle bounds;
    protected EntityDefinition definition; // in case it needs to be cloned

    public Entity(EntityDefinition definition) {
        this.definition = definition;
        this.type = definition.getType();

        float centerX = definition.getCenter().x;
        float centerY = definition.getCenter().y;
        float width = definition.getSize().x;
        float height = definition.getSize().y;
        float left = centerX - (width / 2);
        float bottom = centerY + (height / 2);
        float top = centerY - (height / 2);
        bounds = new Rectangle(left, bottom, width, height);
        leftTop = Vector2Pool.getInstance().obtain(left, top);

        id = getOrCreateId();
        state = State.ACTIVE;

        try {
            body = createBody(definition);
            body.setFixedRotation(definition.getBooleanProperty("fixed_rotation"));
            setAngle(MathUtils.degreesToRadians * definition.getFloatProperty("rotation"));
        } catch (Exception e) {
            Gdx.app.log("entity", "Error creating body for entity with id=" + id);
            Gdx.app.log("entity", e.toString());
        }
    }

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(EntityConstants.PLAYER);
    }

    /**
     * For any necessary post-construction operations (e.g. listening to events)
     */
    public void init() {
        body.setUserData(new BodyData(this));
    }

    @Override
    public void onBeginContact(Contact contact, Entity other) {
    }

    @Override
    public void onEndContact(Contact contact, Entity other) {
    }

    @Override
    public boolean update() {
        tick();
        return isDone();
    }

    @Override
    public void dispose() {
        Level.getInstance().getPhysicsWorld().destroyBody(body);
        Vector2Pool.getInstance().free(leftTop);
        definition.getFixtureDef().shape.dispose();
    }

    public void setTopLeft(float left, float top) {
        setCenter(left + getWidth(), top + getHeight());
    }

    public void setCenter(float x, float y) {
        body.setTransform(x, y, body.getAngle());
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

    public void setActive(boolean active) {
        state = active ? State.ACTIVE : State.INACTIVE;
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

    public void setAngle(float angle) {
        body.setTransform(getCenterX(), getCenterY(), angle);
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
        return bounds.getX() + bounds.getWidth();
    }

    public float getTop() {
        return bounds.getY() - bounds.getHeight();
    }

    public float getBottom() {
        return bounds.getY();
    }

    public float getCenterX() {
        return bounds.getX() + (bounds.getWidth() / 2);
    }

    public float getCenterY() {
        return bounds.getY() + (bounds.getHeight() / 2);
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

    public boolean overlapsPlayer() {
        Level level = Level.getInstance();
        return level.hasPlayer() && overlaps(level.getPlayer());
    }

    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    public float getAngularSpeed() {
        return body.getAngularVelocity();
    }

    public void setAngularSpeed(float speed) {
        body.setAngularVelocity(speed);
    }

    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(x, y);
    }

    public void setPosition(float centerX, float centerY) {
        body.setTransform(centerX, centerY, body.getAngle());
    }

    public void setRotation(float angle) {
        body.setTransform(getCenterX(), getCenterY(), angle);
    }

    protected void tick() {
        float centerX = body.getPosition().x;
        float centerY = body.getPosition().y;
        float left = centerX - (getWidth() / 2);
        float top = centerY - (getHeight() / 2);
        bounds.setPosition(left, top + getHeight());
        leftTop.set(left, top);
    }

    protected Body createBody(EntityDefinition definition) {
        BodyDef bodyDef = definition.getBodyDef();
        bodyDef.position.set(definition.getCenter());
        Body body = Level.getInstance().getPhysicsWorld().createBody(bodyDef);
        FixtureDef fixtureDef = definition.getFixtureDef();
        body.createFixture(fixtureDef);

        return body;
    }

    private String getOrCreateId() {
        String id = definition.getId();
        if (id == null || id.equals("")) {
            id = UUID.randomUUID().toString();
        }

        return id;
    }
}
