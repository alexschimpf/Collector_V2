package com.tendersaucer.collector.event;

import com.tendersaucer.collector.level.ILevelLoadable;

/**
 * Created by Alex on 5/5/2016.
 */
public class LevelLoadBeginEvent extends Event<ILevelLoadBeginListener> {

    private ILevelLoadable loadable;

    public LevelLoadBeginEvent(ILevelLoadable loadable) {
        this.loadable = loadable;
    }

    @Override
    public void notify(ILevelLoadBeginListener listener) {
        listener.onLevelLoadBegin(loadable);
    }
}
