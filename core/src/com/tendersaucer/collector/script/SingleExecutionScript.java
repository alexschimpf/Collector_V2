package com.tendersaucer.collector.script;

/**
 * A script that only executes once
 * <p/>
 * Created by Alex on 4/24/2016.
 */
public abstract class SingleExecutionScript extends Script {

    public SingleExecutionScript(ScriptDefinition def) {
        super(def);
    }

    protected abstract void execute();

    @Override
    public final boolean update() {
        execute();

        return false;
    }
}
