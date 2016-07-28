package com.tendersaucer.collector.util;

import com.badlogic.gdx.Gdx;

/**
 * Created by Alex on 5/5/2016.
 */
public final class Debug {

    private Debug() {
    }

    public static void printDebugInfo() {
        Gdx.app.debug("debug", "FPS: " + Gdx.graphics.getFramesPerSecond());
        Gdx.app.debug("debug", "V2Pool #Free: " + Vector2Pool.getInstance().getFree());
        Gdx.app.debug("debug", "V3Pool #Free: " + Vector2Pool.getInstance().getFree());
        Gdx.app.debug("debug", "Heap size (MB): " + Gdx.app.getJavaHeap() / 1000000.0f);
    }
}
