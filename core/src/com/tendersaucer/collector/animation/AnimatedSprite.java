package com.tendersaucer.collector.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.ConversionUtils;

/**
 * An animated sprite
 *
 * Created by Alex on 4/8/2016.
 */
public class AnimatedSprite extends Sprite implements IUpdate, IRender {

    public enum State {
        PLAYING, PAUSED, STOPPED, FINISHED
    }

    protected float stateTime;
    protected int currNumLoops;
    protected State state;
    protected  Integer numLoops;
    protected com.badlogic.gdx.graphics.g2d.Animation rawAnimation;

    public AnimatedSprite(String key, float totalDuration, Integer numLoops, State state) {
        super();

        Array<AtlasRegion> frames = AssetManager.getInstance().getTextureRegions(key);
        float frameDuration = ConversionUtils.ms2s(totalDuration / frames.size);
        rawAnimation = new com.badlogic.gdx.graphics.g2d.Animation(frameDuration, frames);

        this.numLoops = numLoops;
        if (numLoops == null || numLoops > 1) {
            rawAnimation.setPlayMode(PlayMode.LOOP);
        }

        this.state = state;
    }

    public AnimatedSprite(String key, float totalDuration, Integer numLoops) {
        this(key, totalDuration, numLoops, State.STOPPED);
    }

    public AnimatedSprite(String key, float totalDuration, State state) {
        this(key, totalDuration, 1, state);
    }

    public AnimatedSprite(String key, float totalDuration) {
        this(key, totalDuration, 1);
    }

    public AnimatedSprite(String key) {
        this(key, 0);
    }

    protected AnimatedSprite() {
        super();

        numLoops = 1;
    }

    @Override
    public boolean update() {
        if (!isPlaying()) {
            return false;
        }

        stateTime += Gdx.graphics.getDeltaTime();
        setRegion(getCurrentFrame());

        if (!loops() && rawAnimation.isAnimationFinished(stateTime)) {
            setFinished();
        }

        if (numLoops > 1 && rawAnimation.isAnimationFinished(stateTime)) {
            stateTime = 0;
            if (++currNumLoops == numLoops) {
                setFinished();
            }
        }

        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setOrigin(width / 2, height / 2); // not sure why setOriginCenter doesn't work...
    }

    public void play() {
        if (!isPlaying()) {
            state = State.PLAYING;
            stateTime = 0;
        }
    }

    public void pause() {
        state = State.PAUSED;
    }

    public void resume() {
        state = State.PLAYING;
        if (!isPaused()) {
            stateTime = 0;
        }
    }

    public void stop() {
        state = State.STOPPED;
        stateTime = 0;
    }

    public void setFlipHorizontally(boolean flip) {
        setFlip(flip, isFlipY());
    }

    public void setFlipVertically(boolean flip) {
        setFlip(isFlipX(), flip);
    }

    public boolean isPlaying() {
        return state == State.PLAYING;
    }

    public boolean isPaused() {
        return state == State.PAUSED;
    }

    public boolean isStopped() {
        return state == State.STOPPED;
    }

    public boolean isFinished() {
        return state == State.FINISHED;
    }

    public com.badlogic.gdx.graphics.g2d.Animation getRawAnimation() {
        return rawAnimation;
    }

    public TextureRegion getCurrentFrame() {
        return rawAnimation.getKeyFrame(stateTime);
    }

    public State getState() {
        return state;
    }

    public float getLeft() {
        return getX();
    }

    public float getRigth() {
        return getX() + getWidth();
    }

    public float getTop() {
        return getY() - getHeight();
    }

    public float getBottom() {
        return getY();
    }

    public float getCenterX() {
        return getOriginX();
    }

    public float getCenterY() {
        return getOriginY();
    }

    public int getNumFrames() {
        return rawAnimation.getKeyFrames().length;
    }

    public void setTotalDuration(float totalDuration) {
        float totalDurationSeconds = ConversionUtils.ms2s(totalDuration);
        rawAnimation.setFrameDuration(totalDurationSeconds / getNumFrames());
    }

    /**
     * Sets the number of times this animation will loop
     * @param numLoops - if null, will loop indefinitely
     */
    public void setNumLoops(Integer numLoops) {
        this.numLoops = numLoops;
    }

    public boolean loops() {
        return rawAnimation.getPlayMode() == PlayMode.LOOP;
    }

    protected void setFinished() {
        state = State.FINISHED;
        stateTime = 0;
        currNumLoops = 0;
    }
}
