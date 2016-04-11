package com.tendersaucer.collector.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Conveniently manages related animations
 *
 * Created by Alex on 4/8/2016.
 */
public class AnimationSystem implements IUpdate, IRender, IAnimate {

    public AnimationSystem() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean onDone() {
        return false;
    }
}
