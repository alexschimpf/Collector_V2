package com.tendersaucer.collector.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Wrapper around libgdx's Animation
 *
 * Created by Alex on 4/8/2016.
 */
public class Animation implements IUpdate, IRender, IAnimate {

    public Animation() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {
    }
}
