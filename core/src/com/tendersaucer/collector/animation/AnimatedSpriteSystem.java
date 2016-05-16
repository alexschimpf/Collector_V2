package com.tendersaucer.collector.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tendersaucer.collector.AssetManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Conveniently manages related animations
 *
 * Created by Alex on 4/8/2016.
 */
public final class AnimatedSpriteSystem extends AnimatedSprite {

    private boolean usingDefault;
    private String currentAnimationId;
    private TextureRegion defaultTexture;
    private final Map<String, AnimatedSprite> animationMap;

    public AnimatedSpriteSystem(String defaultTextureName) {
        super();

        defaultTexture = AssetManager.getInstance().getTextureRegion(defaultTextureName);
        animationMap = new HashMap<String, AnimatedSprite>();
        currentAnimationId = defaultTextureName;

        usingDefault = true;
        setRegion(defaultTexture);
    }

    @Override
    public boolean update() {
        if (usingDefault) {
            setRegion(defaultTexture);
            return false;
        }

        return super.update();
    }

    @Override
    public TextureRegion getCurrentFrame() {
        if (usingDefault) {
            return defaultTexture;
        }

        return super.getCurrentFrame();
    }

    public void switchTo(String id, State newState) {
        if (id == null) {
            usingDefault = true;
        } else {
            rawAnimation = animationMap.get(id).getRawAnimation();
        }

        currentAnimationId = id;
        switch (newState) {
            case PLAYING:
                play();
                break;
            case PAUSED:
                pause();
                break;
            case STOPPED:
                stop();
                break;
            case FINISHED:
                setFinished();
                break;
        }
    }

    public void switchToDefault() {
       switchTo(null, State.STOPPED);
    }

    public void add(String id, AnimatedSprite animation) {
        animationMap.put(id, animation);
    }

    public void remove(String id) {
        animationMap.remove(id);
    }

    public void setDefaultTexture(TextureRegion textureRegion) {
        this.defaultTexture = textureRegion;
    }

    public boolean usingDefault() {
        return usingDefault;
    }

    public AnimatedSprite getCurrentAnimation() {
        String id = getCurrentAnimationId();
        if (id == null) {
            return null;
        }

        return animationMap.get(id);
    }

    public String getCurrentAnimationId() {
        if (usingDefault) {
            return null;
        }

        return currentAnimationId;
    }

    public boolean anyPlaying(String... animationIds) {
        for (String animationId : animationIds) {
            if (isPlaying(animationId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPlaying(String animationId) {
        return !usingDefault && getCurrentAnimationId().equals(animationId) &&
                getCurrentAnimation().isPlaying();
    }
}
