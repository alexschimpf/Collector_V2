package com.tendersaucer.collector.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;

/**
 * Game heads up display
 *
 * Created by Alex on 4/8/2016.
 */
public final class HUD implements IUpdate, IRender {

    private static final HUD instance = new HUD();

    private HUD() {
    }

    public static HUD getInstance() {
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
    public void onDone() {
    }

    public void resize(int width, int height) {

    }
}
