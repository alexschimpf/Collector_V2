package com.tendersaucer.collector.script;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IUpdate;

/**
 * A runnable script
 *
 * Created by Alex on 4/24/2016.
 */
public abstract class Script implements IUpdate {

    private Array<IScriptDoneListener> scriptDoneListeners = new Array<IScriptDoneListener>();

    protected Script(ScriptDefinition def) {

    }

    public void clearScriptDoneListeners() {
        scriptDoneListeners.clear();
    }

    public void removeScriptDoneListener(IScriptDoneListener listener) {
        scriptDoneListeners.removeValue(listener, true);
    }

    public void addScriptDoneListener(IScriptDoneListener listener) {
        scriptDoneListeners.add(listener);
    }

    private void notifyScriptDoneListeners() {
        for (IScriptDoneListener listener : scriptDoneListeners) {
            listener.onScriptDone();
        }
    }
}
