package com.tendersaucer.collector.util;

import com.badlogic.gdx.Gdx;

/**
 * Created by Alex on 5/5/2016.
 */
public final class Debug {

    private Debug() {
    }

    public static void printDebugInfo() {
        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        System.out.println("V2Pool #Free: " + Vector2Pool.getInstance().getFree());
        System.out.println("V3Pool #Free: " + Vector2Pool.getInstance().getFree());
        System.out.println("Heap size (MB): " + Gdx.app.getJavaHeap() / 1000000.0f);
    }
}
