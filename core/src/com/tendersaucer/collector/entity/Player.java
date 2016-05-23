package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.animation.AnimatedSpriteSystem;
import com.tendersaucer.collector.world.World;

/**
 * User-controlled player
 * <p/>
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
    private static final String MOVE_ANIMATION_ID = "move";
//  private static final String BLINK_ANIMATION_ID = "blink";

//  private float blinkDelay;
//  private long lastBlinkTime;
    private int numFootContacts;
    private Direction direction;

    public Player(EntityDefinition def) {
        super(def);

        numFootContacts = 0;
//      blinkDelay = getNewBlinkDelay();
//      lastBlinkTime = TimeUtils.millis();
        direction = Direction.RIGHT;
    }

    @Override
    protected void tick() {
        super.tick();

//        if (readyToBlink()) {
//            blink();
//        }

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if (!animationSystem.usingDefault() && (!isMoving() || !animationSystem.isPlaying())) {
            animationSystem.switchToDefault();
        }

        ((AnimatedSpriteSystem)sprite).update();
    }

    @Override
    protected Sprite createSprite(EntityDefinition definition) {
        AnimatedSpriteSystem animationSystem = new AnimatedSpriteSystem("player_default");
        animationSystem.setSize(getWidth(), getHeight());
        animationSystem.add(JUMP_ANIMATION_ID, new AnimatedSprite("player_jump", 250));
        animationSystem.add(MOVE_ANIMATION_ID, new AnimatedSprite("player_move", 400));
        //animationSystem.add(BLINK_ANIMATION_ID, new AnimatedSprite("player_default", 300));

        return animationSystem;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.setFlip(isFacingLeft(), true);
        super.render(spriteBatch);
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

    public void jump() {
        if (!isJumping()) {
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

    public void stopHorizontalMove() {
        setLinearVelocity(0, getLinearVelocity().y);
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }

    private void move(Direction direction) {
        setDirection(direction);

        float vx = Player.MOVE_SPEED * (isFacingLeft() ? -1 : 1);
        setLinearVelocity(vx, getLinearVelocity().y);

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if (!isJumping()) {
            animationSystem.switchTo(MOVE_ANIMATION_ID, AnimatedSprite.State.PLAYING);
        }
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
    }

//    private void blink() {
//        lastBlinkTime = TimeUtils.millis();
//        blinkDelay = getNewBlinkDelay();
//        ((AnimatedSpriteSystem)sprite).switchTo(BLINK_ANIMATION_ID, AnimatedSprite.State.PLAYING);
//    }
//
//    private boolean readyToBlink() {
//        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
//        return animationSystem.usingDefault() && TimeUtils.timeSinceMillis(lastBlinkTime) > blinkDelay;
//    }
//
//    private float getNewBlinkDelay() {
//        return RandomUtils.pickFromRange(2000, 8000);
//    }

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
            Fixture otherFixture =
                    (footSensor == contact.getFixtureA()) ? contact.getFixtureB() : contact.getFixtureA();
            if (!otherFixture.isSensor()) {
                if (onBeginContact) {
                    numFootContacts++;
                } else {
                    numFootContacts--;
                }

                numFootContacts = Math.max(0, numFootContacts);
            }
        }
    }

    private boolean isMoving() {
        return !getLinearVelocity().equals(Vector2.Zero);
    }

    private boolean isJumping() {
        // May have not left the ground yet but is still in the process of jumping
        return numFootContacts < 1 || ((AnimatedSpriteSystem)sprite).isPlaying(JUMP_ANIMATION_ID);
    }

    private Fixture getMainFixture() {
        return body.getFixtureList().get(0);
    }

    private Fixture getFootSensor() {
        return body.getFixtureList().get(1);
    }

    private FixtureDef createFixtureDef(EntityDefinition definition) {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{
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
}
