package com.tendersaucer.collector.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Single particle in an effect
 *
 * Created by Alex on 4/8/2016.
 */
public class Particle implements IUpdate, IRender {

    public Particle() {

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
