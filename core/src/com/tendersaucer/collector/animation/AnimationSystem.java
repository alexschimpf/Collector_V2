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
public final class AnimationSystem extends Animation {

    private boolean usingDefault;
    private final TextureRegion defaultTexture;
    private final Map<String, Animation> animationMap;

    public AnimationSystem(String defaultTextureKey) {
        super();

        defaultTexture = AssetManager.getInstance().getTextureRegion(defaultTextureKey);
        animationMap = new HashMap<String, Animation>();

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
    public TextureRegion getCurrFrame() {
        if (usingDefault) {
            return defaultTexture;
        }

        return super.getCurrFrame();
    }

    public void switchTo(String key) {
        if (key == null) {
            usingDefault = true;
        } else {
            rawAnimation = animationMap.get(key).getRawAnimation();
        }
    }

    public void switchToDefault() {
       switchTo(null);
    }

    public void add(String key, Animation animation) {
        animationMap.put(key, animation);
    }

    public void remove(String key) {
        animationMap.remove(key);
    }

    public boolean usingDefault() {
        return usingDefault;
    }
}
