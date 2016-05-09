package com.tendersaucer.collector.script;

/**
 * Created by Alex on 4/24/2016.
 */
public final class ScriptConfig {

    private static final ScriptConfig instance = new ScriptConfig();

    private ScriptConfig() {
    }

    public static ScriptConfig getInstance() {
        return instance;
    }

    public String getClassName(String type) {
        return null;
    }
}
