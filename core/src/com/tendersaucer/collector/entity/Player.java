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
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.animation.AnimatedSpriteSystem;
import com.tendersaucer.collector.level.Level;

/**
 * User-controlled player
 * Created by Alex on 4/8/2016.
 */
public final class Player extends RenderedEntity {

    private enum Direction {
        LEFT, RIGHT
    }

    private static final String JUMP_ANIMATION_ID = "jump";
    private static final String MOVE_ANIMATION_ID = "move";
    public static final float MOVE_SPEED = 20;
    public static final float JUMP_IMPULSE = -150;
    public static final short COLLISION_MASK = 0x0002;
    private static final float JUMP_ANIMATION_DURATION = 400;
    private static final float MOVE_ANIMATION_DURATION = 200;
    private static final float MAX_FALL_TILES = 9.5f; // don't have to worry about rounding

    private int numFootContacts;
    private Direction direction;
    private float jumpStartY;

    public Player(EntityDefinition def) {
        super(def);

        numFootContacts = 0;
        direction = Direction.RIGHT;
    }

    @Override
    protected void tick() {
        super.tick();

        if (numFootContacts > 0) {
            jumpStartY = getCenterY();
        }

        float maxFallHeight = MAX_FALL_TILES * MainCamera.getInstance().getTileSize();
        if (getCenterY() - jumpStartY > maxFallHeight) {
            respawn();
        }

        ((AnimatedSpriteSystem)sprite).update();
    }

    @Override
    protected Sprite createSprite(EntityDefinition definition) {
        AnimatedSpriteSystem animationSystem = new AnimatedSpriteSystem("player_default");
        animationSystem.setSize(getWidth(), getHeight());
        animationSystem.add(JUMP_ANIMATION_ID, new AnimatedSprite("player_jump", JUMP_ANIMATION_DURATION));
        animationSystem.add(MOVE_ANIMATION_ID, new AnimatedSprite("player_move", MOVE_ANIMATION_DURATION));

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
        Body body = Level.getInstance().getPhysicsWorld().createBody(bodyDef);

        FixtureDef fixtureDef = createFixtureDef();
        body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        body.setBullet(true);
        attachFootSensor(body, definition.getSize().x);

        Filter filter = new Filter();
        filter.categoryBits = 0x0001;
        filter.maskBits = ~Player.COLLISION_MASK;
        body.getFixtureList().get(0).setFilterData(filter);

        return body;
    }

    public void jump() {
        if (!isJumping()) {
            //AssetManager.getInstance().getSound("jump").play();
            body.applyLinearImpulse(0, JUMP_IMPULSE, getCenterX(), getCenterY(), true);

            AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
            if (!animationSystem.isCurrent(JUMP_ANIMATION_ID)) {
                animationSystem.switchTo(JUMP_ANIMATION_ID, AnimatedSprite.State.PLAYING, true);
            }

            jumpStartY = getCenterY();
        }
    }

    public void land() {
        // TODO:
    }

    public void stopJump() {
        if (getLinearVelocity().y < 0) {
            setLinearVelocity(getLinearVelocity().x, 0.02f);
        }

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if (animationSystem.isCurrent(JUMP_ANIMATION_ID)) {
            animationSystem.switchToDefault();
        }
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
        if (!animationSystem.isCurrent(MOVE_ANIMATION_ID) && !isJumping()) {
            animationSystem.switchTo(MOVE_ANIMATION_ID, AnimatedSprite.State.PLAYING);
        } else if (numFootContacts == 0 && animationSystem.isCurrent(MOVE_ANIMATION_ID)) {
            // If moving straight off a surface, without jumping.
            animationSystem.switchToDefault();
        }

        if (!isJumping()) {
            beginMoveParticleEffect();
        }
    }

    public void stopHorizontalMove() {
        setLinearVelocity(0, getLinearVelocity().y);

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if (animationSystem.isCurrent(MOVE_ANIMATION_ID)) {
            animationSystem.switchToDefault();
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

    private void respawn() {
        Level level = Level.getInstance();
        Vector2 respawnPosition = level.getRespawnPosition();
        setLinearVelocity(0, 0.0001f);
        setPosition(respawnPosition.x, respawnPosition.y);

        jumpStartY = getCenterY();
    }

    private void attachFootSensor(Body body, float width) {
        PolygonShape shape = new PolygonShape();
        Vector2 localBottom = body.getLocalPoint(new Vector2(getCenterX(), getBottom()));
        shape.setAsBox(width / 2.1f, 0.12f, localBottom, 0);
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
                    if (numFootContacts == 0) {
                        land();
                    }

                    numFootContacts++;
                } else {
                    numFootContacts--;
                }

                numFootContacts = Math.max(0, numFootContacts);
            }
        }
    }

    private boolean isJumping() {
        // May have not left the ground yet but is still in the process of jumping
        return numFootContacts == 0 || ((AnimatedSpriteSystem)sprite).isPlaying(JUMP_ANIMATION_ID);
    }

    private Fixture getMainFixture() {
        return body.getFixtureList().get(0);
    }

    private Fixture getFootSensor() {
        return body.getFixtureList().get(1);
    }

    private FixtureDef createFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[]{
                new Vector2(1.3f, -1.29f),
                new Vector2(1f, -1.3f),
                new Vector2(-1f, -1.3f),
                new Vector2(-1.3f, -1.29f),
                new Vector2(-1.3f, 1.29f),
                new Vector2(-1f, 1.3f),
                new Vector2(1f, 1.3f),
                new Vector2(1.3f, 1.29f)
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0;

        return fixtureDef;
    }

    private void beginMoveParticleEffect() {
//        Vector2Pool vector2Pool = Vector2Pool.getInstance();
//        Vector2 sizeRange = vector2Pool.obtain(getWidth() / 2, getWidth());
//        Vector2 position = vector2Pool.obtain(getLeft(), getBottom() - (sizeRange.y / 2));
//        ParticleEffect effect =
//                ParticleEffectManager.getInstance().buildParticleEffect(ParticleConstants.PLAYER_MOVE);
//        effect.getVXRange().scl(isFacingLeft() ? 1 : -1);
//        ParticleEffectManager.getInstance().beginParticleEffect(effect, position, sizeRange, 1);
//        vector2Pool.free(position);
//        vector2Pool.free(sizeRange);
    }
}