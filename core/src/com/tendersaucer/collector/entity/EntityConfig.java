package com.tendersaucer.collector.entity;

/**
 * Created by Alex on 4/14/2016.
 */
public class EntityConfig {

    private static EntityConfig instance = new EntityConfig();

    private EntityConfig() {
    }

    public static EntityConfig getInstance() {
        return instance;
    }

    public String getClassName(String type) {
        return "";
    }
}
