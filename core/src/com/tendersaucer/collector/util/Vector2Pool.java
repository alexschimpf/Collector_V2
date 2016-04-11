package com.tendersaucer.collector.util;

/**
 * Simple Vector2 object pool
 *
 * Created by Alex on 4/8/2016.
 */
public final class Vector2Pool {

    private static final Vector2Pool instance = new Vector2Pool();

    private Vector2Pool() {
    }

    public static Vector2Pool getInstance() {
        return instance;
    }
}
