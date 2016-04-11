package com.tendersaucer.collector.ui;

/**
 * Game heads up display
 *
 * Created by Alex on 4/8/2016.
 */
public final class HUD {

    private static final HUD instance = new HUD();

    private HUD() {
    }

    public static HUD getInstance() {
        return instance;
    }
}
