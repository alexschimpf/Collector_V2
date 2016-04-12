package com.tendersaucer.collector.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * System of particles
 *
 * Created by Alex on 4/8/2016.
 */
public class ParticleEffect implements IUpdate, IRender {

    public ParticleEffect() {

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
