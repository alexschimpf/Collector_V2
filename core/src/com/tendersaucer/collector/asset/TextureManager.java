package com.tendersaucer.collector.asset;

/**
 * Manages game textures
 *
 * Created by Alex on 4/8/2016.
 */
public final class TextureManager {

    private static final TextureManager instance = new TextureManager();

    private TextureManager() {
    }

    public static TextureManager getInstance() {
        return instance;
    }
}
