package com.tendersaucer.collector.asset;

/**
 * Manages game music
 *
 * Created by Alex on 4/8/2016.
 */
public final class MusicManager {

    private static final MusicManager instance = new MusicManager();

    private MusicManager() {
    }

    public static MusicManager getInstance() {
        return instance;
    }
}
