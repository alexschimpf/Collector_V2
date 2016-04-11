package com.tendersaucer.collector;

/**
 * Main game camera
 *
 * Created by Alex on 4/8/2016.
 */
public class Camera {

    private static final Camera instance = new Camera();

    private Camera() {
    }

    public static Camera getInstance() {
        return instance;
    }
}
