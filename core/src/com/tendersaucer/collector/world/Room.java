package com.tendersaucer.collector.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

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
    public void onDone() {
    }
}
