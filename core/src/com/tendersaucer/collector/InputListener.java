package com.tendersaucer.collector;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * Game input listener
 *
 * Created by Alex on 4/8/2016.
 */
public final class InputListener extends com.badlogic.gdx.scenes.scene2d.InputListener implements IUpdate {

    private static final InputListener instance = new InputListener();

    private InputListener() {
    }

    public InputListener getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {

    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        return false;
    }

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        return false;
    }
}
