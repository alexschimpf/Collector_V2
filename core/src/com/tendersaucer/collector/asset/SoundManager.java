package com.tendersaucer.collector.asset;

/**
 * Manages game sounds
 *
 * Created by Alex on 4/8/2016.
 */
public final class SoundManager {

    private static final SoundManager instance = new SoundManager();

    private SoundManager() {
    }

    public static SoundManager getInstance() {
        return instance;
    }
}
