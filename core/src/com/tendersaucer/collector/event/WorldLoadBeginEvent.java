package com.tendersaucer.collector.event;

import com.tendersaucer.collector.world.IWorldLoadable;

/**
 * Created by Alex on 5/5/2016.
 */
public class WorldLoadBeginEvent extends Event<IWorldLoadBeginListener> {

    private final IWorldLoadable loadable;

    public WorldLoadBeginEvent(IWorldLoadable loadable) {
        this.loadable = loadable;
    }

    @Override
    public void notify(IWorldLoadBeginListener listener) {
        listener.onWorldLoadBegin(loadable);
    }
}
