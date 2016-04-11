package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Manages rooms
 *
 * Created by Alex on 4/8/2016.
 */
public final class World implements IUpdate, IRender {

    private static final World instance = new World();

    private World() {
    }

    public static World getInstance() {
        return instance;
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
