package com.tendersaucer.collector.event;

import com.tendersaucer.collector.script.Script;

/**
 * Created by Alex on 5/5/2016.
 */
public final class ScriptDoneEvent extends Event<IScriptDoneListener> {

    private Script script;

    public ScriptDoneEvent(Script script) {
        this.script = script;
    }

    @Override
    public void notify(IScriptDoneListener listener) {
        listener.onScriptDone(script);
    }
}
