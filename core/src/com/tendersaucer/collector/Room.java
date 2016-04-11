package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Sub-levels of a world
 *
 * Created by Alex on 4/8/2016.
 */
public class Room implements IUpdate, IRender {

    public Room() {

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
