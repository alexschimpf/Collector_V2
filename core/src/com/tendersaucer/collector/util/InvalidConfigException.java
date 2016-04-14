package com.tendersaucer.collector.util;

/**
 * Unchecked exception for an invalid configuration
 *
 * Created by Alex on 4/13/2016.
 */
public class InvalidConfigException extends RuntimeException {

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(String configName, String property, Object value) {
        super(configName + ": " + property + " = " + (value != null ? value.toString() : "null"));
    }
}
