package com.tendersaucer.collector;

import com.tendersaucer.collector.screen.Driver;

/**
 * Game entry point
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class Game extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        setScreen(Driver.getInstance());
    }
}
