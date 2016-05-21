package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.animation.AnimatedSpriteSystem;
import com.tendersaucer.collector.util.RandomUtils;
import com.tendersaucer.collector.world.World;

/**
 * User-controlled player
 *
 * Created by Alex on 4/8/2016.
 */
public final class Player extends RenderedEntity {

    private enum Direction {
        LEFT, RIGHT
    }

    public static final float MOVE_SPEED = 20;
    public static final float JUMP_IMPULSE = -150;
    public static final float MASS = 5.69f;
    public static final short COLLISION_MASK = 0x0002;
    public static final String TYPE = "player";
    private static final String JUMP_ANIMATION_ID = "jump";
    private static final String BLINK_ANIMATION_ID = "blink";
    private static final String MOVE_ANIMATION_ID = "move";

    private float blinkDelay;
    private long lastBlinkTime;
    private boolean isJumping;
    private int numFootContacts;
    private Direction direction;

    public Player(EntityDefinition def) {
        super(def);

        blinkDelay = getNewBlinkDelay();
        lastBlinkTime = TimeUtils.millis();
        numFootContacts = 0;
        isJumping = false;
        direction = Direction.RIGHT;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected void tick() {
        super.tick();

        if (readyToBlink()) {
            blink();
        }

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if(!animationSystem.anyPlaying(MOVE_ANIMATION_ID, JUMP_ANIMATION_ID, BLINK_ANIMATION_ID)) {
            animationSystem.switchToDefault();
        }

        animationSystem.update();
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {
        handleFootContact(contact, true);
    }

    @Override
    public void onEndContact(Contact contact, Entity entity) {
        handleFootContact(contact, false);
    }

    @Override
    protected Sprite createSprite(EntityDefinition definition) {
        AnimatedSpriteSystem animationSystem = new AnimatedSpriteSystem("default");
        animationSystem.setSize(getWidth(), getHeight());
        animationSystem.add(JUMP_ANIMATION_ID, new AnimatedSprite("default", 300));
        animationSystem.add(MOVE_ANIMATION_ID, new AnimatedSprite("default", 300));
        animationSystem.add(BLINK_ANIMATION_ID, new AnimatedSprite("default", 300));

        return animationSystem;
    }

    @Override
    protected Body createBody(EntityDefinition definition) {
        BodyDef bodyDef = definition.getBodyDef();
        bodyDef.position.set(definition.getCenter());
        Body body = World.getInstance().getPhysicsWorld().createBody(bodyDef);

        FixtureDef fixtureDef = createFixtureDef(definition);
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        body.setBullet(true);
        body.getMassData().mass = MASS;
        attachFootSensor(body, definition.getSize().x);

        Filter filter = new Filter();
        filter.categoryBits = 0x0001;
        filter.maskBits = ~Player.COLLISION_MASK;
        body.getFixtureList().get(0).setFilterData(filter);

        return body;
    }

    private FixtureDef createFixtureDef(EntityDefinition definition) {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[] {
            new Vector2(0.9f, -1.29f),
            new Vector2(0.6f, -1.3f),
            new Vector2(-0.6f, -1.3f),
            new Vector2(-0.9f, -1.29f),
            new Vector2(-0.9f, 1.29f),
            new Vector2(-0.6f, 1.3f),
            new Vector2(0.6f, 1.3f),
            new Vector2(0.9f, 1.29f)
        });

        definition.getFixtureDef().shape.dispose();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0;

        return fixtureDef;
    }

    public boolean isFacingLeft() {
        return direction == Direction.LEFT;
    }

    public boolean isFacingRight() {
        return direction == Direction.RIGHT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        ((AnimatedSpriteSystem)sprite).setFlipHorizontally(isFacingLeft());
    }

    public void jump() {
        if (!isJumping) {
            AssetManager.getInstance().getSound("jump").play();
            body.applyLinearImpulse(0, JUMP_IMPULSE, getCenterX(), getCenterY(), true);
            ((AnimatedSpriteSystem)sprite).switchTo(JUMP_ANIMATION_ID, AnimatedSprite.State.PLAYING);
        }
    }

    public void stopJump() {
        if (getLinearVelocity().y < 0) {
            setLinearVelocity(getLinearVelocity().x, 0.02f);
        }
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }

    public void stopMove() {
        setLinearVelocity(0, getLinearVelocity().y);

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if(!animationSystem.anyPlaying(JUMP_ANIMATION_ID, BLINK_ANIMATION_ID)) {
            ((AnimatedSpriteSystem)sprite).switchToDefault();
        }
    }

    private void move(Direction direction) {
        setDirection(direction);

        float vx = Player.MOVE_SPEED * (isFacingLeft() ? -1 : 1);
        setLinearVelocity(vx, getLinearVelocity().y);

        // Update sprite.
        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if(numFootContacts > 0 && !isJumping &&
                !animationSystem.anyPlaying(MOVE_ANIMATION_ID, JUMP_ANIMATION_ID)) {
            animationSystem.switchTo(MOVE_ANIMATION_ID, AnimatedSprite.State.PLAYING);
        } else if(numFootContacts == 0 &&
                !animationSystem.anyPlaying(JUMP_ANIMATION_ID, BLINK_ANIMATION_ID)) {
            animationSystem.switchToDefault();
        }
    }

    private boolean readyToBlink() {
        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        return animationSystem.usingDefault() && TimeUtils.timeSinceMillis(lastBlinkTime) > blinkDelay;
    }

    private void blink() {
        lastBlinkTime = TimeUtils.millis();
        blinkDelay = getNewBlinkDelay();
        ((AnimatedSpriteSystem)sprite).switchTo(BLINK_ANIMATION_ID, AnimatedSprite.State.PLAYING);
    }

    private float getNewBlinkDelay() {
        return RandomUtils.pickFromRange(2000, 8000);
    }

    private void attachFootSensor(Body body, float width) {
        PolygonShape shape = new PolygonShape();
        Vector2 localBottom = body.getLocalPoint(new Vector2(getCenterX(), getBottom()));
        shape.setAsBox(width / 2 * 0.83f, 0.12f, localBottom, 0);
        Fixture fixture = body.createFixture(shape, 0);
        fixture.setSensor(true);

        shape.dispose();
    }

    private void handleFootContact(Contact contact, boolean onBeginContact) {
        Fixture footSensor = getFootSensor();
        if (contact.getFixtureA() == footSensor || contact.getFixtureB() == footSensor) {
            Fixture otherFixture = contact.getFixtureA();
            if (otherFixture == footSensor) {
                otherFixture = contact.getFixtureB();
            }

            if(!otherFixture.isSensor()) {
                if (onBeginContact) {
                    isJumping = ++numFootContacts < 1;
                } else {
                    isJumping = Math.max(0, --numFootContacts) < 1;
                }
            }
        }
    }

    private Fixture getMainFixture() {
        return body.getFixtureList().get(0);
    }

    private Fixture getFootSensor() {
        return body.getFixtureList().get(1);
    }
}
