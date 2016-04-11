package com.tendersaucer.collector.entity;

/**
 * Validates entity properties from loadable against config
 *
 * Created by Alex on 4/8/2016.
 */
public final class EntityPropertyValidator {

    private static final EntityPropertyValidator instance = new EntityPropertyValidator();

    private EntityPropertyValidator() {
    }

    public static EntityPropertyValidator getInstance() {
        return instance;
    }
}
