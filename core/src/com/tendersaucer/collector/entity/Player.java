package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.animation.AnimatedSpriteSystem;
import com.tendersaucer.collector.util.RandomUtils;

/**
 * User-controlled player
 *
 * Created by Alex on 4/8/2016.
 */
public final class Player extends RenderedEntity {

    private enum Direction {
        LEFT, RIGHT
    }

    public static final float MOVE_SPEED = 10;
    public static final float JUMP_IMPULSE = -98;
    public static final float MOVE_PARTICLE_DELAY = 100;
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

        // TODO: Set sprite as AnimatedSprite
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    @Override
    protected void tick() {
        super.tick();

        if (readyToBlink()) {
            blink();
        }

        AnimatedSpriteSystem animationSystem = (AnimatedSpriteSystem)sprite;
        if(!animationSystem.isPlaying(JUMP_ANIMATION_ID)) {
            animationSystem.switchToDefault();
        }

        animationSystem.update();
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {
        numFootContacts++;
    }

    @Override
    public void onEndContact(Contact contact, Entity entity) {
        numFootContacts--;
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

    /*
     * If not moving downward, reduce speed (but not to a halt).
     */
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
}
