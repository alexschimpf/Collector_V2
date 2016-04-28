package com.tendersaucer.collector.script;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IUpdate;

/**
 * A runnable script
 *
 * Created by Alex on 4/24/2016.
 */
public abstract class Script implements IUpdate {

    private Array<IScriptDoneListener> scriptDoneListeners;

    protected Script(ScriptDefinition def) {

    }

    public void addScriptDoneListener(IScriptDoneListener listener) {
        if (scriptDoneListeners == null) {
            scriptDoneListeners = new Array<IScriptDoneListener>();
        }

        scriptDoneListeners.add(listener);
    }

    private void notifyScriptDoneListeners() {
        if (scriptDoneListeners == null) {
            return;
        }

        for (IScriptDoneListener listener : scriptDoneListeners) {
            listener.onScriptDone();
        }
    }
}
