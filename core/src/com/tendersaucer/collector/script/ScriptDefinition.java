package com.tendersaucer.collector.script;

/**
 * Interface for scripts
 * <p/>
 * Created by Alex on 4/9/2016.
 */
public abstract class ScriptDefinition {

    protected String name;
    protected String type;

    public ScriptDefinition(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public abstract Object getProperty(String key);

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
