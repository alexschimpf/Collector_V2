package com.tendersaucer.collector.event;

import com.tendersaucer.collector.script.Script;

/**
 * Created by Alex on 4/24/2016.
 */
public interface IScriptDoneListener {

    void onScriptDone(Script script);
}
