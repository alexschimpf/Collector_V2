package com.tendersaucer.collector.world;

import com.badlogic.gdx.maps.MapProperties;

/**
 * Validates entity properties from loadable against config
 *
 * Created by Alex on 4/8/2016.
 */
public final class TiledEntityPropertyValidator {

    private static final TiledEntityPropertyValidator instance = new TiledEntityPropertyValidator();

    private TiledEntityPropertyValidator() {
    }

    public static TiledEntityPropertyValidator getInstance() {
        return instance;
    }

    public void validate(String type, MapProperties properties) {

    }
}