package com.tendersaucer.collector.util;

/**
 * Unchecked exception, indicating an invalid configuration
 *
 * Created by Alex on 4/13/2016.
 */
public final class InvalidConfigException extends RuntimeException {

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(String configName, String property, Object value) {
        super(configName + ": " + property + " = " + (value != null ? value.toString() : "null"));
    }
}
